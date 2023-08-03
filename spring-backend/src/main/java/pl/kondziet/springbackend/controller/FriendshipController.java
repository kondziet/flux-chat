package pl.kondziet.springbackend.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

        Mono<String> userEmail = ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Principal::getName);

        return userEmail
                .flatMap(userRepository::findUserByEmail)
                .map(User::getId)
                .flatMap(sender -> friendshipService.sendFriendshipRequest(sender, receiverId))
                .then(Mono.just(
                        ResponseEntity.ok("hello")
                ));
    }

    @PostMapping("/accept/{friendshipId}")
    Mono<ResponseEntity<String>> acceptFriendshipRequest(@PathVariable String friendshipId) {

        Mono<String> userId = ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Principal::getName)
                .flatMap(userRepository::findUserByEmail)
                .map(User::getId);

        return userId
                .flatMap(id -> friendshipService.acceptFriendshipRequest(friendshipId, id))
                .then(Mono.just(
                        ResponseEntity.ok("done")
                ));
    }


}
