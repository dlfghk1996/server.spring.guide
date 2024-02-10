package server.spring.guide.request.resolver.convert;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;

// HandlerMethodArgumentResolver 구현시 Converter는 적용 안되는 문제 해결
/* Converter를 호출하는 로직은 AbstractNamedValueMethodArgumentResolver가 수행하고 있으므로
   AbstractNamedValueMethodArgumentResolve 상속 받아 구현한다. */
public class CustomAbstractNamedValueMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver {


    // annotation 속성의 값(name , required , defaultvalue )을 추출해 저장한다.
    @Override
    protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        Role annotation = parameter.getParameterAnnotation(Role.class);
        return annotation != null ? new RoleNamedValueInfo(annotation) : new RoleNamedValueInfo();
    }

    //  resolveArgument 에서 핵심 로직 구현 부분
    @Override
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request.getNativeRequest();
        Role annotation = parameter.getParameterAnnotation(Role.class);
        assert annotation != null;
        String RoleName = annotation.name();
        return httpServletRequest.getHeader(RoleName);
    }


    // 호출되는 Controller의 파라미터 타입을 검사하는 콜백 함수로, resolveArgument 메서드 실행여부 판단
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(Role.class) != null;
    }

    private static class RoleNamedValueInfo extends NamedValueInfo {

        public RoleNamedValueInfo() {
            super("", false, ValueConstants.DEFAULT_NONE);
        }

        public RoleNamedValueInfo(Role annotation) {
            super(annotation.name(), annotation.required(), ValueConstants.DEFAULT_NONE);
        }
    }
}
