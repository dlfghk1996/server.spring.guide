package server.spring.guide.annotation;//package server.spring.argumentResolver;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 특정한 부분에서만 자동으로 객체가 Argument Resolver를 통해
 * 바인딩되도록 만들고 싶은 경우 커스텀 어노테이션을 만들어서 해결할 수 있다
 *
 * 속성
 * @Ingerited : 부모 클래스에 해당 Annotation이 선언됐다면 자식클래스에게도 상속
 * @Retention(value=RetentionPolicy.RUNTIME) : 해당 Annotation의 정보 유지 범위
 * @Target(ElementType.METHOD) : 해당 Annotation을 사용가능한 대상
 * */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomAnnotaion {
    String message();
}
