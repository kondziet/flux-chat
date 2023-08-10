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
public class AcceptedFriendshipResponse {

    private String friendshipId;
    private String friendId;
    private String friendUsername;
}
