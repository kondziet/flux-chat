package pl.kondziet.springbackend.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kondziet.springbackend.model.entity.User;
import pl.kondziet.springbackend.repository.UserRepository;
import pl.kondziet.springbackend.service.UserService;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Mono<Boolean> doesUserWithEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Mono<Boolean> doesUserWithUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Mono<Boolean> doesUserWithIdExists(String id) {
        return userRepository.existsById(id);
    }

    @Override
    public Mono<User> saveUser(User user) {
        return userRepository.save(user);
    }
}
