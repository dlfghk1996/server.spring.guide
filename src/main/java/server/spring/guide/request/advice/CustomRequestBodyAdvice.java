package server.spring.guide.request.advice;

import java.io.IOException;
import java.lang.reflect.Type;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import server.spring.guide.common.domain.User;

// RequestBodyAdvice를 이용하면 Http 메세지를 객체로 변환하기 전/후 또는 body가 비어있을 때 등을 처리할 수 있다.
//예를 들어 클라이언트가 보내는 json 요청을 위한 User 클래스가 있다고 하자.
//우리가 하고 싶은 동작은 ArgumentResolver를 이용하여 @RequestBody로 만들어진 user 객체의 값 중에서 desc만 바꿔주는 것이다.

@RestControllerAdvice
public class CustomRequestBodyAdvice implements RequestBodyAdvice {

    // 해당 RequestBodyAdvice를 적용할지 여부를 결정함
    @Override
    public boolean supports(final MethodParameter methodParameter, final Type targetType, final Class<? extends HttpMessageConverter<?>> converterType) {
        return methodParameter.getContainingClass() == AdviceController.class && targetType.getTypeName().equals(User.class.getTypeName());
    }

    // body를 읽어 객체로 변환되기 전에 호출됨
    @Override
    public HttpInputMessage beforeBodyRead(final HttpInputMessage inputMessage, final MethodParameter parameter, final Type targetType, final Class<? extends HttpMessageConverter<?>> converterType) {
        return inputMessage;
    }

    // body를 읽어 객체로 변환된 후에 호출됨
    @Override
    public Object afterBodyRead(final Object body, final HttpInputMessage inputMessage, final MethodParameter parameter, final Type targetType, final Class<? extends HttpMessageConverter<?>> converterType) {
        final User user = (User) body;
        user.setName("desc");
        return user;
    }

    // body가 비어있을때 호출됨
    @Override
    public Object handleEmptyBody(final Object body, final HttpInputMessage inputMessage, final MethodParameter parameter, final Type targetType, final Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
    //출처: https://mangkyu.tistory.com/250 [MangKyu's Diary:티스토리]
}
