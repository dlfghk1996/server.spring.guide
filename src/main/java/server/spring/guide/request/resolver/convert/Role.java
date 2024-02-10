package server.spring.guide.request.resolver.convert;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Role {
    //기본값을 지정하지 않으면 필수입니다.
    String name() default "";

    boolean required();
}