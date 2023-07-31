package pl.kondziet.springbackend.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.kondziet.springbackend.model.enumerable.MessageType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Message {
    @Id
    private String id;
    private MessageType type;
    private String content;
    private String sender;
}
