package server.spring.guide.argumentResolver.basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestInfo {

    private Long userId;

    private String ip;

    private String agent;
}
