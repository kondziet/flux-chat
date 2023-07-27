package pl.kondziet.springbackend.model.entity;

import lombok.*;
import pl.kondziet.springbackend.model.enumerable.MessageType;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Message {
    private MessageType type;
    private String content;
    private String sender;
}
