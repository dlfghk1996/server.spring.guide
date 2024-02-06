package server.spring.guide.common.dto;



import lombok.Data;
@Data
public class UserLikeUpDTO {

    private Long userId;
    private String userName;
    private Double count;


}
