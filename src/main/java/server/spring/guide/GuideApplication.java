package server.spring.guide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.beans.factory.config.Scope;
import server.spring.guide.bean.BeanConfig;
import server.spring.guide.bean.CustomTreadScope;
import server.spring.guide.bean.UserBean;

@EnableScheduling

@SpringBootApplication
public class GuideApplication {
	public static void main(String[] args) {
		SpringApplication.run(GuideApplication.class, args);
	}
}



