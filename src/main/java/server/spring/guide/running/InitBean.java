package server.spring.guide.running;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Data
@Component
public class InitBean implements InitializingBean {

    private static final Logger LOG =
        LoggerFactory.getLogger(InitBean.class);
    @Autowired
    private Environment environment;

    public InitBean() {
        System.out.println("InvalidInitExampleBean Construstor");
    //  java.lang.NullPointerException 발생 : InitBean 생성자가 호출되는 시점에서 Environment bean 이 초기화 되지 않았다.
    //  environment.getActiveProfiles();
    }

// Caused by: java.lang.NullPointerException:
// Cannot invoke "server.spring.guide.running.InvalidInitExampleBean.print()"
// because "server.spring.guide.GuideApplication.invalidInitExampleBean" is null
    public void print(){
   
        System.out.println("!!");
    }

    // 해결방법 1 : @PostConstruct
    // 초기화 콜백: 빈이 생성되고 빈의 의존관계 주입이 완료된 후의 호출한다.
    @PostConstruct
    public void envPrint() throws Exception {
        System.out.println(environment.getActiveProfiles());
    }

    // 해결방법 2: Constructor Injection
    @Autowired
    public InitBean(Environment environment) {
        this.environment = environment;
        LOG.info(environment.getActiveProfiles().toString());
    }

    // 해결방법 3 : @Bean(initMethod="init")
    // @Bean 어노테이션을 이용하여 InitBean 빈등록시 아래 메서드를 콜백메서드로 지정
    public void init() {
        LOG.info("init-method");
    }

    // 해결방법 4: InitializingBean.afterPropertiesSet()
    @Override
    public void afterPropertiesSet() throws Exception {
        LOG.info("InitializingBean");
    }
}
