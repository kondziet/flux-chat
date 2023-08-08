package pl.kondziet.springbackend.model.aggregation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kondziet.springbackend.model.enumerable.FriendshipStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendshipDetails {
    private String id;
    private String senderId;
    private String receiverId;
    private FriendshipStatus friendshipStatus;
    private String senderUsername;
}
