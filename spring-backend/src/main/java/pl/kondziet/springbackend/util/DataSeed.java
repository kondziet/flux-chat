package pl.kondziet.springbackend.util;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.kondziet.springbackend.model.entity.Message;
import pl.kondziet.springbackend.model.entity.User;
import pl.kondziet.springbackend.model.enumerable.MessageType;
import pl.kondziet.springbackend.repository.MessageRepository;
import pl.kondziet.springbackend.repository.UserRepository;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class DataSeed implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        User user1 = User.builder()
                .email("dwightkschrute@yahoo.com")
                .password(passwordEncoder.encode("beetroot"))
                .username("dwight")
                .visibleNickname("beetroot-farm")
                .build();

        userRepository.save(user1).subscribe(savedUser -> {
            System.out.println("User saved successfully: " + savedUser);
        });

        Message message1 = Message.builder()
                .type(MessageType.CHAT)
                .content("Hello guys :)")
                .sender("Mike")
                .build();

        messageRepository.save(message1).subscribe(savedMessage -> {
            System.out.println("Message saved successfully: " + savedMessage);
        });
    }
}
