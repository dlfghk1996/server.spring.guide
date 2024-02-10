package server.spring.guide.request.resolver.basic;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import server.spring.guide.common.dto.UserDTO;

/** HandlerMethodArgumentResolver
 *  컨트롤러 메서드에서 특정 조건에 맞는 파라미터가 있을 때 요청에 들어온 값을 이용해 원하는 값을 바인딩 한다.
 * ex. @RequestBody, @PathVariable
 * **/

// 예제 : parameter 로 넘어온 회원 정보와 ip를 requetInfo에 바인딩하여 return 한다.
@Slf4j
@Component
public class AuthCustomArgumentResolver implements HandlerMethodArgumentResolver {

    /** 호출되는 Controller의 파라미터 값을 검사하는 콜백 함수
     * supportsParameter: 들어온 파라미터에 대해 resolveArgument 메서드 실행여부 판단
     * ex. 리턴 값이 true => resolveArgument 메서드 실행
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return
            // getParameterType() : 컨트롤러 메소드의 파라미터가 UserDTO 타입인지 검사
            parameter.getParameterType().equals(UserDTO.class) ||
                // hasParameterAnnotation() : 컨트롤러 메소드의 파라미터가  @AuthUser 어노테이션을 가지고 있는지 확인한다.
                parameter.hasParameterAnnotation(AuthUser.class); // @AuthenticationPrincipal 예시

    }

    /* 파라미터를 가공하는 역할: 반환값이 대상이 되는 메소드의 파라미터에 바인딩 */
    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();

        // RequestParameter value(=name) 로 사용자 PK 를 조회하여 바인딩한다.
        // SCOPE_REQUEST,REFERENCE_REQUEST, SCOPE_REQUEST
        String name = (String) webRequest.getAttribute("name", WebRequest.SCOPE_REQUEST);
        // TODO select * from tbl_user where name = :name

        if(parameter.hasParameterAnnotation(AuthUser.class)){
              // Annotation 으로 header value를 추출하는 것도 가능하다.
//            AuthUser annotation = parameter.getParameterAnnotation(AuthUser.class);
//            String authUserName = annotation.name();
//            request.getHeader(authUserName);
            return 1L;
        }
        return new RequestInfo(1L, getClientIP(request), request.getHeader("User-Agent"));
    }

    public static String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
