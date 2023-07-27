package pl.kondziet.springbackend.util;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.kondziet.springbackend.model.entity.User;
import pl.kondziet.springbackend.repository.UserRepository;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class DataSeed implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        User user1 = User.builder()
                .nickName("dwight")
                .email("dwightkschrute@yahoo.com")
                .password(passwordEncoder.encode("beetroot"))
                .build();

        userRepository.save(user1).subscribe(savedUser -> {
            System.out.println("User saved successfully: " + savedUser);
        });
    }
}
