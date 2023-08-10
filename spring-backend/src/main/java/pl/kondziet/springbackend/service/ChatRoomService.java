package pl.kondziet.springbackend.service;

import pl.kondziet.springbackend.model.entity.ChatRoom;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface ChatRoomService {

    Mono<ChatRoom> createChatRoom(String name, Set<String> memberIds);
}
