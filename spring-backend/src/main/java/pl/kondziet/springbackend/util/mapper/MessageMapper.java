package pl.kondziet.springbackend.util.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import pl.kondziet.springbackend.model.entity.Message;

@Component
public class MessageMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public Message toMessage(String message) {
        return objectMapper.readValue(message, Message.class);
    }

    @SneakyThrows
    public String toString(Message event) {
        return objectMapper.writeValueAsString(event);
    }
}
