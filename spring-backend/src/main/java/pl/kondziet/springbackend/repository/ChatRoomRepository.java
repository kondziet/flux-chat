package pl.kondziet.springbackend.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.kondziet.springbackend.model.entity.ChatRoom;

public interface ChatRoomRepository extends ReactiveMongoRepository<ChatRoom, String> {
}
