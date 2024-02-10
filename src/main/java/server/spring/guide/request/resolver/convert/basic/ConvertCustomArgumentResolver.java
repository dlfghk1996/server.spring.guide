package server.spring.guide.request.resolver.convert.basic;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import server.spring.guide.request.resolver.convert.Role;

/*
* 예제
* 요청 메서드 파라미터의 @Role value을 가져와서 가공 후 controller 에 전달
*
* 문제 :ArgumentResolver가 해당 타입을 상속하지 않은 경우 Converter를 활용할 수 없다.
* @PathVariable, @RequestParam 은  AbstractNamedValueMethodArgumentResolver 상속받아 설계되어있다.
* */
public class ConvertCustomArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(Role.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest httpServletRequest = (HttpServletRequest)webRequest.getNativeRequest();
        Role annotation = parameter.getParameterAnnotation(Role.class);
        assert annotation != null;
        // @Role 의 value값을 String 타입으로 받아온다.
        String headerName = annotation.name();
        return httpServletRequest.getHeader(headerName);
    }
}