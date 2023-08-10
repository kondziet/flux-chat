package pl.kondziet.springbackend.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kondziet.springbackend.model.entity.ChatRoom;
import pl.kondziet.springbackend.repository.ChatRoomRepository;
import pl.kondziet.springbackend.repository.UserRepository;
import pl.kondziet.springbackend.service.ChatRoomService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@AllArgsConstructor
@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Override
    public Mono<ChatRoom> createChatRoom(String name, Set<String> memberIds) {
        return userRepository
                .existsAllById(memberIds)
                .filter(allExists -> allExists)
                .switchIfEmpty(
                        Mono.error(new IllegalArgumentException("Some members may not exist"))
                )
                .then(
                        chatRoomRepository.save(
                                ChatRoom.builder()
                                        .name(name)
                                        .memberIds(memberIds)
                                        .build()
                        )
                );
    }

    @Override
    public Flux<ChatRoom> findAllUserChatRooms(String userId) {
        return chatRoomRepository.findByMemberIdsContaining(userId);
    }
}
