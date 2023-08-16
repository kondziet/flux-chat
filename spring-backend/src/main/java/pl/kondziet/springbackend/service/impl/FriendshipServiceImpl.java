package pl.kondziet.springbackend.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kondziet.springbackend.model.aggregation.FriendshipDetails;
import pl.kondziet.springbackend.model.entity.Friendship;
import pl.kondziet.springbackend.model.enumerable.FriendshipStatus;
import pl.kondziet.springbackend.repository.FriendshipRepository;
import pl.kondziet.springbackend.service.FriendshipService;
import pl.kondziet.springbackend.service.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class FriendshipServiceImpl implements FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserService userService;

    @Override
    public Mono<Friendship> sendFriendshipRequest(String senderId, String receiverId) {

        Mono<Boolean> isSenderReceiver = Mono.just(senderId.equals(receiverId))
                .filter(same -> !same)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Sender is also Receiver")));

        Mono<Boolean> requestAlreadySent = Mono.zip(
                        friendshipRepository.existsBySenderIdAndReceiverId(senderId, receiverId),
                        friendshipRepository.existsBySenderIdAndReceiverId(receiverId, senderId)
                )
                .map(tuple -> tuple.getT1() || tuple.getT2())
                .filter(sent -> !sent)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Request has been already sent")));

        return Mono.zip(
                        isSenderReceiver,
                        requestAlreadySent
                )
                .then(friendshipRepository.save(
                                Friendship.builder()
                                        .senderId(senderId)
                                        .receiverId(receiverId)
                                        .friendshipStatus(FriendshipStatus.REQUESTED)
                                        .build()
                        )
                );
    }

    @Override
    public Mono<Friendship> acceptFriendshipRequest(String friendshipId, String accepterId) {
        return friendshipRepository
                .findById(friendshipId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(String.format("Friendship with ID %s doesn't exist", friendshipId))))
                .flatMap(friendship -> {
                    if (friendship.getReceiverId().equals(accepterId)) {
                        friendship.setFriendshipStatus(FriendshipStatus.ACCEPTED);
                        return friendshipRepository.save(friendship);
                    } else {
                        return Mono.error(new IllegalArgumentException("User is not allowed to accept this friendship request"));
                    }
                });
    }

    @Override
    public Mono<Void> declineFriendshipRequest(String friendshipId, String declinerId) {
        return friendshipRepository
                .findById(friendshipId)
                .map(friendship -> friendship.getReceiverId().equals(declinerId) || friendship.getSenderId().equals(declinerId))
                .flatMap(isAllowed -> {
                    if (isAllowed) {
                        return friendshipRepository.deleteById(friendshipId);
                    } else {
                        return Mono.error(new IllegalArgumentException("User is not allowed for decline this friendship request"));
                    }
                });
    }

    @Override
    public Flux<FriendshipDetails> findAllFriendshipsWithUserId(String userId, FriendshipStatus friendshipStatus) {
        return friendshipRepository.findAllFriendshipsContainingUserId(userId, friendshipStatus);
    }

    @Override
    public Flux<FriendshipDetails> findReceiverFriendshipDetails(String receiverId, FriendshipStatus friendshipStatus) {
        return friendshipRepository.findReceiverFriendships(receiverId, friendshipStatus);
    }

    @Override
    public Flux<FriendshipDetails> findSenderFriendshipDetails(String senderId, FriendshipStatus friendshipStatus) {
        return friendshipRepository.findSenderFriendships(senderId, friendshipStatus);
    }
}
