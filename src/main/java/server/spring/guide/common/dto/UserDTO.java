package server.spring.guide.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.spring.guide.aop.masking.annotation.Mask;
import server.spring.guide.aop.masking.enums.MaskingType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {

    private Long id;

    @Mask(type = MaskingType.PHONE_NUMBER)
    private String phoneNum;

    @Mask(type = MaskingType.EMAIL)
    private String email;

    private String status;

    @Mask(type = MaskingType.NAME)
    private String name;

    private String password;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime signUpdate;

    private boolean married;

    public UserDTO(Long id, String userName, String phoneNum, String email) {
        this.id = id;
        this.name = userName;
        this.phoneNum = phoneNum;
        this.email = email;
    }
}
