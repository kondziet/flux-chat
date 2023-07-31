package pl.kondziet.springbackend.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import pl.kondziet.springbackend.model.entity.User;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Mono<UserDetails> findByEmail(String email);
    Mono<UserDetails> findByUsername(String username);
}
