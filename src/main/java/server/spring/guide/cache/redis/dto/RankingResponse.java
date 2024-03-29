package server.spring.guide.cache.redis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankingResponse {
    private Long userId;
    private String userName;
    private Long raking;
    private int count;
}
