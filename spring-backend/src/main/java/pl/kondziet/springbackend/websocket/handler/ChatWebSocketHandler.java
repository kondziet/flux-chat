package pl.kondziet.springbackend.websocket.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import pl.kondziet.springbackend.model.entity.Message;
import pl.kondziet.springbackend.model.enumerable.MessageType;
import pl.kondziet.springbackend.util.mapper.MessageMapper;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@Component
public class ChatWebSocketHandler implements WebSocketHandler {

    private final Sinks.Many<Message> chatHistory = Sinks.many().replay().limit(30);
    private final MessageMapper messageMapper;


    @Override
    public Mono<Void> handle(WebSocketSession session) {
        AtomicReference<Message> lastReceivedEvent = new AtomicReference<>();

        Mono<Void> receive = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .map(messageMapper::toMessage)
                .doOnNext(message -> {
                    System.out.println(message);
                    lastReceivedEvent.set(message);
                    chatHistory.tryEmitNext(message);
                })
                .doOnComplete(() -> {
                    System.out.println("Completed");

                    if (lastReceivedEvent.get() != null) {
                        Message userLeave = Message.builder()
                                .sender(lastReceivedEvent.get().getSender())
                                .content("Bye!")
                                .type(MessageType.LEAVE)
                                .build();

                        chatHistory.tryEmitNext(userLeave);
                    }
                })
                .then();

        Mono<Void> send = session.send(chatHistory.asFlux()
                .map(messageMapper::toString)
                .map(session::textMessage))
                .then();

        return Mono.zip(receive, send)
                .doOnTerminate(() -> {
                    System.out.println("Session handling has been completed!");
                })
                .then();
    }

}
