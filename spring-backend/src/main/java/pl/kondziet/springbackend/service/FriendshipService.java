package pl.kondziet.springbackend.service;

import pl.kondziet.springbackend.model.entity.Friendship;
import reactor.core.publisher.Mono;

public interface FriendshipService {

    Mono<Friendship> sendFriendshipRequest(String senderId, String receiverId);
    Mono<Friendship> acceptFriendshipRequest(String friendshipId, String accepterId);
}
