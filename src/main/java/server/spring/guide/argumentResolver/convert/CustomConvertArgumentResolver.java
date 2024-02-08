package server.spring.guide.argumentResolver.convert;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;
import server.spring.guide.argumentResolver.basic.AuthUser;

// 단순히 ArgumentResolver를 구현하는 것의 문제점
// ArgumentResolver를 구현해서 등록하면 ArgumentResolver는 잘 동작한다.
// 그러나 Converter는 제대로 동작하지 않는다.
// 그러나 만약 enum 타입으로 인자를 받고 @Role 어노테이션을 사용하면 인자가 일치하지 않는다는 에러를 보게 된다.

public class CustomConvertArgumentResolver extends AbstractNamedValueMethodArgumentResolver {

//    첫번 째는 createNamedValueInfo이다.
//    이는 AbstractNamedValueInfo에서 사용하며 annotation 속성에서 name , required , defaultvalue 속성의 값을
//    추출해 저장한다.
//    이는 resolveArgument에서 활용한다.
//    이를 활용해 내가 정의한 어노테이션의 속성 입력에 따라 Spring에서 구현한 로직의 도움을 받을 수 있다.
    @Override
    protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        Role annotation = parameter.getParameterAnnotation(Role.class);
        return annotation != null ? new RoleNamedValueInfo(annotation) : new RoleNamedValueInfo();
    }

    // 핵심 로직을 구현하는 부분이다.
    @Override
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request.getNativeRequest();
        Role annotation = parameter.getParameterAnnotation(Role.class);
        assert annotation != null;
        String RoleName = annotation.name();
        return httpServletRequest.getHeader(RoleName);
    }

    // 세 번째는 supportsParameter로 해당 ArgumentResolver가
    // 지원하는 MethodParameter인가 확인하는 메서드이다. 해당 메서드의 구현은 @Header라는 이름이 붙은
    // 어노테이션 파라미터에 적용하고 싶기 때문에 내가 정의한 어노테이션이 parameter에 있는지 확인한다.
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
