package pl.kondziet.springbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kondziet.springbackend.model.enumerable.MessageType;

@Data
@AllArgsConstructor
public class MessageRequest {

    private String content;
    private MessageType messageType;

}
