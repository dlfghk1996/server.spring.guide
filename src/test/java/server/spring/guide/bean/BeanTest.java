package server.spring.guide.bean;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import server.spring.guide.common.domain.User;
import server.spring.guide.common.dto.UserDTO;

@ActiveProfiles(value = "local")
@SpringBootTest
public class BeanTest {


    @Autowired
    private ApplicationContext context;

    @Test
    public void prototype_bean_in_singleton_bean_test() {

        System.out.println("proto");
        System.out.println(context.getBean(BeanConfig.class));
        System.out.println(context.getBean(BeanConfig.class));

        System.out.println("single");
        System.out.println(context.getBean(SingletonScopeBean.class));
        System.out.println(context.getBean(SingletonScopeBean.class));

        System.out.println("proto type by singleton");
        System.out.println(context.getBean(SingletonScopeBean.class).getProto());
        System.out.println(context.getBean(SingletonScopeBean.class).getProto());
    }

    @Test
    public void custom_bean_test() {

        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(BeanConfig.class);

        // This code will be executed by child thread
        Runnable childThread = () -> {
            String threadName = Thread.currentThread().getName();
            UserBean v1 = ac.getBean("userBean", UserBean.class);
            UserBean v2 = ac.getBean("userBean", UserBean.class);
            v2.setName("홍길동");
            System.out.println(threadName + " 하위 스레드에서 생성된 두 객체의 해시코드");
            System.out.println(v1.hashCode() + " & " + v2.hashCode());
            System.out.println("하위 스레드에 의해 생성된 두 개체가 모두 동일한가요 ? :" + (v1.hashCode() == v2.hashCode()));
        };
        new Thread(childThread).start();

        // This code will be executed by main thread

        UserBean v1 = ac.getBean("userBean", UserBean.class);
        System.out.println(v1.getName());
        UserBean v2 = ac.getBean("userBean", UserBean.class);

        System.out.println( Thread.currentThread().getName() + " 메인 스레드에서 생성된 두 객체의 해시코드");
        System.out.println(v1.hashCode() + " & " + v2.hashCode());
        System.out.println("메인 스레드에 의해 생성된 두 개체가 모두 동일한가요? :" + (v1.hashCode() == v2.hashCode()));
    }
}
