package server.spring.guide;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import server.spring.guide.running.InitBean;

@EnableScheduling

@SpringBootApplication
public class GuideApplication {

	//우선 static 필드에 @Autowired를 사용하지 못하는 이유는 정적 필드가 클래스 로더에 의해
	// 인스턴스화 될 때 아직 Spring Context는 로드되지 않았기 때문에 Spring 프레임워크에 의해 관리되는 @Autowired가 동작하지 않는 것이었다.
	@Autowired
	static InitBean invalidInitExampleBean;

//	static 은 new() 명령으로 객체를 생성(인스턴스화)하지 않아도 바로 쓸 수 있습니다.
//
//	static 이기 때문에 이미 인스턴스화되어 있어
//	다른 Java 페이지 등에서 마치 전역함수인 것처럼 편하게 호출해 쓸 수 있기 때문에 이런식으로 쓰려고 하면서 에러가 나는 것입니다.

	public static void main(String[] args) {
		SpringApplication.run(GuideApplication.class, args);

//		invalidInitExampleBean.print();
	}
}



