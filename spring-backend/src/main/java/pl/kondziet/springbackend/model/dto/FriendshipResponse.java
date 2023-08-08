package pl.kondziet.springbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kondziet.springbackend.model.enumerable.FriendshipStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendshipResponse {

    private String id;
    private String senderId;
    private String receiverId;
    private FriendshipStatus friendshipStatus;
    private String senderUsername;
    private String receiverUsername;
}
