package pl.kondziet.springbackend.util.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import pl.kondziet.springbackend.model.dto.MessageRequest;
import pl.kondziet.springbackend.model.dto.MessageResponse;

@Component
public class MessageMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public MessageRequest toMessageRequest(String messageRequest) {
        return objectMapper.readValue(messageRequest, MessageRequest.class);
    }

    @SneakyThrows
    public String toString(MessageRequest messageRequest) {
        return objectMapper.writeValueAsString(messageRequest);
    }

    @SneakyThrows
    public MessageResponse toMessageResponse(String messageResponse) {
        return objectMapper.readValue(messageResponse, MessageResponse.class);
    }

    @SneakyThrows
    public String toString(MessageResponse messageResponse) {
        return objectMapper.writeValueAsString(messageResponse);
    }
}
