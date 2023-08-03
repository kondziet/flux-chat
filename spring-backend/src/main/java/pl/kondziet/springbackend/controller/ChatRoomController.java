package pl.kondziet.springbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("/api/chatroom")
public class ChatRoomController {

    @PostMapping
    public Mono<ResponseEntity<String>> createNewChatRoom() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Principal::getName)
                .doOnNext(System.out::println)
                .flatMap(auth -> Mono.just(ResponseEntity.ok(auth)));
    }
}
