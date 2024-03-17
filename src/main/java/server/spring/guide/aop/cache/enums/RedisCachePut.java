package server.spring.guide.aop.cache.enums;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.val;

/**
 * 메서드 반환 값을 캐시 저장한다.
 * 메서드 실행중 예외 발생시 캐시가 저장되지 않는다.
 * 캐시를 업데이트하는 어노테이션이다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisCachePut {
    /**
     * redis에 사용할 캐시 이름
     */
    String cacheName = null;
    /**
     * 캐시 만료 시간 (초 단위) default : 만료시간 없음
     */
    Long expireSecond = -1L;
    /**
     *  캐시 key 생성에 class, method 이름을 사용할 것 인지 default : false
     * classAndMethodNamePrefix = true -> key::ClassName.MethodName(args...)
     * classAndMethodNamePrefix = false -> key::(args...)
     */
    Boolean hasClassAndMethodNamePrefix = false;
}

