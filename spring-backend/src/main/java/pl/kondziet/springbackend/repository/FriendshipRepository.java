package pl.kondziet.springbackend.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.kondziet.springbackend.model.entity.Friendship;

public interface FriendshipRepository extends ReactiveMongoRepository<Friendship, String> {
}
