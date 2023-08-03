package pl.kondziet.springbackend.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kondziet.springbackend.exception.UserNotFoundException;
import pl.kondziet.springbackend.model.entity.Friendship;
import pl.kondziet.springbackend.model.enumerable.FriendshipStatus;
import pl.kondziet.springbackend.repository.FriendshipRepository;
import pl.kondziet.springbackend.service.FriendshipService;
import pl.kondziet.springbackend.service.UserService;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class FriendshipServiceImpl implements FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserService userService;

    @Override
    public Mono<Boolean> sendFriendshipRequest(String senderId, String receiverId) {
        return userService.doesUserWithIdExists(receiverId)
                .flatMap(exists -> {
                    if (exists) {
                        return friendshipRepository.save(
                                Friendship.builder()
                                        .senderId(senderId)
                                        .receiverId(receiverId)
                                        .friendshipStatus(FriendshipStatus.REQUESTED)
                                        .build()
                        );
                    } else {
                        return Mono.error(new UserNotFoundException(String.format("User with ID %s doesn't exist", receiverId)));
                    }
                })
                .then(Mono.just(true));
    }
}
