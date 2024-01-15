package server.spring.guide.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class UserDTO {
    private Long id;
    private String userStatus;
    private String userId;
    private String userName;
    private String userPassword;
}
