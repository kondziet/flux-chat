package pl.kondziet.springbackend.websocket.handler;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import pl.kondziet.springbackend.model.dto.MessageResponse;
import pl.kondziet.springbackend.model.entity.Message;
import pl.kondziet.springbackend.repository.MessageRepository;
import pl.kondziet.springbackend.util.mapper.MessageMapper;
import pl.kondziet.springbackend.chat.ChatSinksManager;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.concurrent.atomic.AtomicReference;
import java.security.Principal;

import org.springframework.security.core.context.SecurityContext;

@AllArgsConstructor
@Component
public class DirectMessageHandler implements WebSocketHandler {
    private final ChatSinksManager chatSinksManager;
    private final MessageMapper messageMapper;
    private final MessageRepository messageRepository;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String chatRoomId = extractChatRoomIdFromSession(session);
        Sinks.Many<Message> chatSinks = chatSinksManager.getOrCreateChatSinks(chatRoomId);

        Mono<String> authenticatedUserId = ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Principal::getName);

        AtomicReference<Message> lastReceivedEvent = new AtomicReference<>();

        Mono<Void> receive = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .map(messageMapper::toMessageRequest)
                .flatMap(messageRequest -> authenticatedUserId.map(userId -> Message.builder()
                        .senderId(userId)
                        .content(messageRequest.getContent())
                        .build()))
                .doOnNext(message -> {
                    System.out.println(message);
                    lastReceivedEvent.set(message);
                    chatSinks.tryEmitNext(message);
                })
                .flatMap(messageRepository::save)
                .doOnComplete(() -> {
                    System.out.println("Completed");

                    if (lastReceivedEvent.get() != null) {
                        Message userLeave = Message.builder()
                                .senderId(lastReceivedEvent.get().getSenderId())
                                .content("Bye!")
                                .build();

                        chatSinks.tryEmitNext(userLeave);
                    }
                })
                .then();

        Mono<Void> send = session.send(chatSinks.asFlux()
                        .map(message -> MessageResponse.builder()
                                .content(message.getContent())
                                .senderId(message.getSenderId())
                                .build()
                        )
                        .map(messageMapper::toString)
                        .map(session::textMessage))
                .then();

        return Mono.zip(receive, send)
                .doOnTerminate(() -> {
                    System.out.println("Session handling has been completed!");
                })
                .then();
    }

    private String extractChatRoomIdFromSession(WebSocketSession session) {
        String path = session.getHandshakeInfo().getUri().getPath();

        String[] parts = path.split("/");
        return parts[2];
    }
}
