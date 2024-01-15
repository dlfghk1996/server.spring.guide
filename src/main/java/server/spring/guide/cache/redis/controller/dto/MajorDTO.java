package server.spring.guide.cache.redis.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
public class MajorDTO {

    private String id;

    private String name;

    private long ttl;
}
