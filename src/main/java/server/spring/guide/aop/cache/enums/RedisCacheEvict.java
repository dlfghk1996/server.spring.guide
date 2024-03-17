package server.spring.guide.aop.cache.enums;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.val;

/**
 * 메서드 실행 후 캐시를 제거한다.
 * 메서드 실행중 예외 발생시 캐시가 제거되지 않는다.
 * 캐시를 제거하는 어노테이션이다.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public  @interface RedisCacheEvict {
    /**
     * redis에 사용할 캐시 이름
     */
    String cacheName = null;

    /**
     *  clearAll = true : 메서드 parameter에 관계 없이 해당 key로 전부 제거
     *  clearAll = false : 메서드 parameter와 일치하는 캐시만 제거
     */
    Boolean clearAll = null;
    /**
     *  캐시 key 생성에 class, method 이름을 사용할 것 인지 default : false
     * classAndMethodNamePrefix = true -> key::ClassName.MethodName(args...)
     * classAndMethodNamePrefix = false -> key::(args...)
     */
    // clearAll옵션은 key와 일치하는 캐시를 전부 삭제할지 여부이다.
    // redis에 실제로 저장돼 key는 여기의 key 필드를 prefix로 설정된다.
    // 뒤에는 파라미터 정보를 포함하고 클래스, 메서드 정보를 선택적으로 포함한다.
    Boolean hasClassAndMethodNamePrefix = null;
}


