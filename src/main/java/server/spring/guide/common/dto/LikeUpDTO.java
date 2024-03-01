package server.spring.guide.common.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeUpDTO {

    private Long userId;
    private String userName;
    private int count;


}
