package server.spring.guide.request.resolver.basic;

import org.springframework.web.bind.annotation.GetMapping;
import server.spring.guide.request.resolver.convert.enums.UserType;
import server.spring.guide.common.dto.UserDTO;

public class ArgumentTestController {

    // 매핑 일치 기준 1: 어노테이션
    @GetMapping("")
    public void test1(@AuthUser int requestId) {
        System.out.println(requestId);
    }

    // 매핑 일치 기준 2 : 어노테이션 + enum
    // 어노테이션 속성 값 정의
    @GetMapping("")
    public void test1(@AuthUser UserType userType) {
        System.out.println(userType);
    }

    // 매핑 일치 기준 : Class Type
    @GetMapping("")
    public void test3(UserDTO request) {
        System.out.println(request.toString());
    }

}
