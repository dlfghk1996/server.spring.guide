package server.spring.guide.cache.redis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankingResponse {
    private String userName;
    private String raking;
    private Double count;

    public RankingResponse(String userName, Double count) {
        this.userName = userName;
        this.count = count;
    }
}
