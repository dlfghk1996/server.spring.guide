package server.spring.guide.aop.masking.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


// 마스킹을 적용시키고 싶은 메서드에 사용할 마스킹 어노테이션
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplyMasking {

    Class<?> typeValue();
    Class<?> genericTypeValue() default Void.class; // <Generic> 사용시
}
