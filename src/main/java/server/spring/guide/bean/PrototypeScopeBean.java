package server.spring.guide.bean;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import server.spring.guide.common.service.UserService;

@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class PrototypeScopeBean {

    @Autowired
    private UserService userService;

    public PrototypeScopeBean(UserService userService) {
        System.out.println("beanConfig 생성자");
    }

    // 초기화 콜백: 빈이 생성되고 빈의 의존관계 주입이 완료된 후의 호출
    @PostConstruct
    public void init() throws Exception {

        System.out.println(
            "Bean HelloWorld has been "
                + "instantiated and I'm the "
                + "init() method");
    }

    // 소멸전 콜백
    @PreDestroy
    public void destroy() throws Exception {

        System.out.println(
            "Container has been closed "
                + "and I'm the destroy() method");
    }
}
