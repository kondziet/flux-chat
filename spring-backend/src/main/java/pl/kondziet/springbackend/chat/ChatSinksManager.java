package pl.kondziet.springbackend.chat;

import org.springframework.stereotype.Component;
import pl.kondziet.springbackend.model.entity.Message;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatSinksManager {

    private final Map<String, Sinks.Many<Message>> chatSinksMap = new ConcurrentHashMap<>();

    public Sinks.Many<Message> getOrCreateChatSinks(String chatRoomId) {
        return chatSinksMap.computeIfAbsent(chatRoomId, key -> Sinks.many().multicast().directBestEffort());
    }
}
