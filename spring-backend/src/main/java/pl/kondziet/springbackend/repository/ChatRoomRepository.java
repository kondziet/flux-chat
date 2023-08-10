package pl.kondziet.springbackend.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.kondziet.springbackend.model.entity.ChatRoom;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ChatRoomRepository extends ReactiveMongoRepository<ChatRoom, String> {

    Flux<ChatRoom> findByMemberIdsContaining(String memberId);
}
