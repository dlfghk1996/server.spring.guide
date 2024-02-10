package server.spring.guide.request.resolver.convert;

import org.springframework.web.bind.annotation.GetMapping;
import server.spring.guide.request.resolver.convert.enums.UserType;

public class ConvertArgumentTestController {

    // 매핑 일치 기준 2 : 어노테이션 + enum
    // 어노테이션 속성 값 정의
    @GetMapping("")
    public void test2(@Role(name = "userType", required = false) UserType userType) {
        System.out.println(userType);
    }
}
