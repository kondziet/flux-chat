package pl.kondziet.springbackend.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import pl.kondziet.springbackend.model.entity.User;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findByUsername(String username);
    Mono<User> findUserByEmail(String email);
    Mono<UserDetails> findByEmail(String email);
    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByUsername(String username);
    Mono<Boolean> existsAllById(Set<String> userIds);
}
