package server.spring.guide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAspectJAutoProxy
public class GuideApplication {
	public static void main(String[] args) {
		SpringApplication.run(GuideApplication.class, args);
	}

}



