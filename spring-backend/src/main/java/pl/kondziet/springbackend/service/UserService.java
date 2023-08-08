package pl.kondziet.springbackend.service;

import pl.kondziet.springbackend.model.entity.User;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<Boolean> doesUserWithEmailExists(String email);
    Mono<Boolean> doesUserWithUsernameExists(String username);
    Mono<User> saveUser(User user);
}
