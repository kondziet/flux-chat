package pl.kondziet.springbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {

    private String nickName;
    private String email;
    private String password;
}
