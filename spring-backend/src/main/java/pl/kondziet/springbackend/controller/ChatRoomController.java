package pl.kondziet.springbackend.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import pl.kondziet.springbackend.model.dto.ChatRoomResponse;
import pl.kondziet.springbackend.model.dto.CreateChatRoomRequest;
import pl.kondziet.springbackend.model.entity.User;
import pl.kondziet.springbackend.repository.UserRepository;
import pl.kondziet.springbackend.service.ChatRoomService;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/chatroom")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final UserRepository userRepository;

    @PostMapping("/create")
    public Mono<ResponseEntity<String>> createNewChatRoom(@RequestBody CreateChatRoomRequest createChatRoomRequest) {
        return chatRoomService
                .createChatRoom(
                        createChatRoomRequest.getName(),
                        createChatRoomRequest.getMemberIds()
                )
                .thenReturn(ResponseEntity.ok("Chat room has been created")
                )
                .onErrorResume(
                        IllegalArgumentException.class,
                        e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()))
                );
    }

    @GetMapping
    Mono<ResponseEntity<List<ChatRoomResponse>>> getChatRooms() {
        Mono<String> authenticatedUserId = ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Principal::getName)
                .flatMap(userRepository::findUserByEmail)
                .map(User::getId);

        Mono<List<ChatRoomResponse>> userChatRooms = authenticatedUserId
                .flatMapMany(chatRoomService::findAllUserChatRooms)
                .map(chatRoom -> ChatRoomResponse.builder()
                        .chatRoomId(chatRoom.getId())
                        .name(chatRoom.getName())
                        .build()
                )
                .collectList();

        return userChatRooms
                .map(ResponseEntity::ok);
    }
}
