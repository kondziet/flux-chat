package pl.kondziet.springbackend.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.kondziet.springbackend.model.entity.Message;

public interface MessageRepository extends ReactiveMongoRepository<Message, String> {
}
