package pl.kondziet.springbackend.service;

import pl.kondziet.springbackend.model.aggregation.FriendshipDetails;
import pl.kondziet.springbackend.model.entity.Friendship;
import pl.kondziet.springbackend.model.enumerable.FriendshipStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FriendshipService {

    Flux<FriendshipDetails> findReceiverFriendshipDetails(String receiverId, FriendshipStatus friendshipStatus);
    Mono<Friendship> sendFriendshipRequest(String senderId, String receiverId);
    Mono<Friendship> acceptFriendshipRequest(String friendshipId, String accepterId);
}
