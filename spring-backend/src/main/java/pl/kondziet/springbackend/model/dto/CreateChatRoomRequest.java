package pl.kondziet.springbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class CreateChatRoomRequest {

    private String name;
    private Set<String> memberIds;
}
