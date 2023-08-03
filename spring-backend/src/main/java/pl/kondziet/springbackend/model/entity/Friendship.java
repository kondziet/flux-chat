package pl.kondziet.springbackend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.kondziet.springbackend.model.enumerable.FriendshipStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Friendship {

    @Id
    private String id;
    private String senderId;
    private String receiverId;
    private FriendshipStatus friendshipStatus;

}
