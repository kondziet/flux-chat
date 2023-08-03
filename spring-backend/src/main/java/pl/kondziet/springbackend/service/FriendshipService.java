package pl.kondziet.springbackend.service;

import reactor.core.publisher.Mono;

public interface FriendshipService {

    Mono<Boolean> sendFriendshipRequest(String senderId, String receiverId);
}
