package pl.kondziet.springbackend.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import pl.kondziet.springbackend.model.dto.AcceptedFriendshipResponse;
import pl.kondziet.springbackend.model.dto.PendingFriendshipResponse;
import pl.kondziet.springbackend.model.dto.RequestedFriendshipResponse;
import pl.kondziet.springbackend.model.entity.User;
import pl.kondziet.springbackend.model.enumerable.FriendshipStatus;
import pl.kondziet.springbackend.repository.UserRepository;
import pl.kondziet.springbackend.service.FriendshipService;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/friendship")
public class FriendshipController {

    private final FriendshipService friendshipService;
    private final UserRepository userRepository;

    @PostMapping("/request/{receiverUsername}")
    Mono<ResponseEntity<String>> sendFriendshipRequest(@PathVariable String receiverUsername) {

        Mono<String> authenticatedUserId = ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Principal::getName)
                .flatMap(userRepository::findUserByEmail)
                .map(User::getId);

        return userRepository.findByUsername(receiverUsername)
                .flatMap(receiver -> {
                    String receiverId = receiver.getId();
                    return authenticatedUserId
                            .flatMap(senderId -> friendshipService.sendFriendshipRequest(senderId, receiverId))
                            .thenReturn(ResponseEntity.ok("Friendship request has been sent"))
                            .onErrorResume(
                                    IllegalArgumentException.class,
                                    e -> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()))
                            );
                })
                .switchIfEmpty(
                        Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with given username doesn't exist"))
                );
    }

    @PostMapping("/accept/{friendshipId}")
    Mono<ResponseEntity<String>> acceptFriendshipRequest(@PathVariable String friendshipId) {

        Mono<String> authenticatedUserId = ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Principal::getName)
                .flatMap(userRepository::findUserByEmail)
                .map(User::getId);

        return authenticatedUserId
                .flatMap(accepterId -> friendshipService.acceptFriendshipRequest(friendshipId, accepterId))
                .thenReturn(ResponseEntity.ok("Friendship request has been accepted"))
                .onErrorResume(
                        IllegalArgumentException.class,
                        e -> Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage()))
                );
    }

    @PostMapping("/decline/{friendshipId}")
    Mono<ResponseEntity<String>> declineFriendshipRequest(@PathVariable String friendshipId) {

        Mono<String> authenticatedUserId = ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Principal::getName)
                .flatMap(userRepository::findUserByEmail)
                .map(User::getId);

        return authenticatedUserId
                .flatMap(declinerId -> friendshipService.declineFriendshipRequest(friendshipId, declinerId))
                .thenReturn(ResponseEntity.ok("Friendship request has been declined"))
                .onErrorResume(
                        IllegalArgumentException.class,
                        e -> Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage()))
                );
    }

    @GetMapping("/pending")
    Mono<ResponseEntity<List<PendingFriendshipResponse>>> getPendingFriendships() {

        Mono<String> authenticatedUserId = ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Principal::getName)
                .flatMap(userRepository::findUserByEmail)
                .map(User::getId);

        Mono<List<PendingFriendshipResponse>> friendshipRequests = authenticatedUserId
                .flatMapMany(receiverId -> friendshipService.findReceiverFriendshipDetails(receiverId, FriendshipStatus.REQUESTED))
                .map(request -> PendingFriendshipResponse.builder()
                        .friendshipId(request.getId())
                        .senderId(request.getSenderId())
                        .senderUsername(request.getSenderUsername())
                        .build()
                )
                .collectList();

        return friendshipRequests
                .map(ResponseEntity::ok);
    }

    @GetMapping("/requested")
    Mono<ResponseEntity<List<RequestedFriendshipResponse>>> getRequestedFriendships() {

        Mono<String> authenticatedUserId = ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Principal::getName)
                .flatMap(userRepository::findUserByEmail)
                .map(User::getId);

        Mono<List<RequestedFriendshipResponse>> friendshipRequests = authenticatedUserId
                .flatMapMany(senderId -> friendshipService.findSenderFriendshipDetails(senderId, FriendshipStatus.REQUESTED))
                .map(request -> RequestedFriendshipResponse.builder()
                        .friendshipId(request.getId())
                        .receiverId(request.getReceiverId())
                        .receiverUsername(request.getReceiverUsername())
                        .build()
                )
                .collectList();

        return friendshipRequests
                .map(ResponseEntity::ok);
    }

    @GetMapping("/accepted")
    Mono<ResponseEntity<List<AcceptedFriendshipResponse>>> getAcceptedFriendships() {

        Mono<String> authenticatedUserId = ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Principal::getName)
                .flatMap(userRepository::findUserByEmail)
                .map(User::getId);

        Mono<List<AcceptedFriendshipResponse>> friendshipRequests = authenticatedUserId
                .flatMapMany(userId -> friendshipService.findAllFriendshipsWithUserId(userId, FriendshipStatus.ACCEPTED)
                        .map(request -> {
                                    if (request.getSenderId().equals(userId)) {
                                        return AcceptedFriendshipResponse.builder()
                                                .friendshipId(request.getId())
                                                .friendId(request.getReceiverId())
                                                .friendUsername(request.getReceiverUsername())
                                                .build();
                                    } else {
                                        return AcceptedFriendshipResponse.builder()
                                                .friendshipId(request.getId())
                                                .friendId(request.getSenderId())
                                                .friendUsername(request.getSenderUsername())
                                                .build();
                                    }
                                }
                        ))

                .collectList();

        return friendshipRequests
                .map(ResponseEntity::ok);
    }
}
