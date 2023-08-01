package pl.kondziet.springbackend.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.kondziet.springbackend.model.entity.ChatRoom;
import reactor.core.publisher.Flux;

public interface ChatRoomRepository extends ReactiveMongoRepository<ChatRoom, String> {

    Flux<ChatRoom> findByUserIdsContaining(String userId);
}
