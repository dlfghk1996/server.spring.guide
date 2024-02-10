package server.spring.guide.request.resolver.basic;

import java.lang.reflect.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import server.spring.guide.common.dto.UserDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestInfo {

    private Long userId;

    private String ip;

    private String agent;
}
