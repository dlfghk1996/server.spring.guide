//package server.spring.guide.aop.cache.enums;
//
//import static org.eclipse.jdt.internal.compiler.lookup.TagBits.AnnotationTarget;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//import lombok.val;
//import org.modelmapper.internal.bytebuddy.implementation.attribute.AnnotationRetention;
//
///**
// * 메서드 반환 값을 캐시 저장한다.
// * 이미 저장된 캐시가 있다면 캐시 값을 리턴한다.
// * 메서드 실행중 예외 발생시 캐시가 저장되지 않는다.
// * 캐시를 저장하거나 캐시가 있다면 반환하는 어노테이션이다.
// */
//@Target(ElementType.METHOD)
//@Retention(RetentionPolicy.RUNTIME)
//public  @interface RedisCacheable {
//    /**
//     * redis에 사용할 캐시 이름
//     */
//    String cacheName = null;
//    /**
//     * 캐시 만료 시간 (초 단위) default : 만료시간 없음
//     */
//    Long expireSecond = -1L;
//    /**
//     *  캐시 key 생성에 class, method 이름을 사용할 것 인지 default : false
//     * classAndMethodNamePrefix = true -> key::ClassName.MethodName(args...)
//     * classAndMethodNamePrefix = false -> key::(args...)
//     */
//    // hasClassAndMethodNamePrefix는 캐시 key에 클래스와 메서드의 이름을 붙일지 정하는 옵션이다.
//    // 이 옵션은 캐시 키의 이름이 중복될 가능성이 있어 설정이 필요할 때가 있다.
//    // 자세한 이유는 https://jgrammer.tistory.com/entry/무신사-watcher-Cacheable-중복되는-key값-어떻게-처리할까
//    Boolean hasClassAndMethodNamePrefix = false;
//}
//
//
