package pl.kondziet.springbackend.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.kondziet.springbackend.model.entity.Friendship;
import reactor.core.publisher.Mono;

public interface FriendshipRepository extends ReactiveMongoRepository<Friendship, String> {

    Mono<Boolean> existsBySenderIdAndReceiverId(String senderId, String receiverId);
}
