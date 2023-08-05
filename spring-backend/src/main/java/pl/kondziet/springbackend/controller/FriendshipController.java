package pl.kondziet.springbackend.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kondziet.springbackend.exception.UserAlreadyExistsException;
import pl.kondziet.springbackend.exception.UserNotFoundException;
import pl.kondziet.springbackend.model.dto.RegisterResponse;
import pl.kondziet.springbackend.model.entity.User;
import pl.kondziet.springbackend.repository.UserRepository;
import pl.kondziet.springbackend.service.FriendshipService;
import reactor.core.publisher.Mono;

import java.security.Principal;

@AllArgsConstructor
@RestController
@RequestMapping("/api/friendship")
public class FriendshipController {

    private final FriendshipService friendshipService;
    private final UserRepository userRepository;

    @PostMapping("/request/{receiverId}")
    Mono<ResponseEntity<String>> sendFriendshipRequest(@PathVariable String receiverId) {

        Mono<String> clientId = ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Principal::getName)
                .flatMap(userRepository::findUserByEmail)
                .map(User::getId);

        return clientId
                .flatMap(senderId -> friendshipService.sendFriendshipRequest(senderId, receiverId))
                .thenReturn(ResponseEntity.ok("Friendship request has been sent"))
                .onErrorResume(
                        UserNotFoundException.class,
                        e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()))
                )
                .onErrorResume(
                        IllegalArgumentException.class,
                        e -> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()))
                );
    }

    @PostMapping("/accept/{friendshipId}")
    Mono<ResponseEntity<String>> acceptFriendshipRequest(@PathVariable String friendshipId) {

        Mono<String> clientId = ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Principal::getName)
                .flatMap(userRepository::findUserByEmail)
                .map(User::getId);

        return clientId
                .flatMap(accepterId -> friendshipService.acceptFriendshipRequest(friendshipId, accepterId))
                .thenReturn(ResponseEntity.ok("Friendship has been accepted"))
                .onErrorResume(
                        IllegalArgumentException.class,
                        e -> Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage()))
                );
    }


}
