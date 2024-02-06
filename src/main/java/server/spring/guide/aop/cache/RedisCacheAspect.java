//package server.spring.guide.aop.cache;
//
//// AOP 로 캐시의 만료시간 관리
//
//import java.lang.reflect.Method;
//import java.util.concurrent.TimeUnit;
//import lombok.val;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.util.StringUtils;
//import server.spring.guide.aop.cache.enums.RedisCacheEvict;
//import server.spring.guide.aop.cache.enums.RedisCachePut;
//import server.spring.guide.aop.cache.enums.RedisCacheable;
//
///**
// * 메서드 반환 값을 캐시 저장한다.
// * 이미 저장된 캐시가 있다면 캐시 값을 리턴한다.
// * 메서드 실행중 예외 발생시 캐시가 저장되지 않는다.
// */
////결론
////캐시에 ttl을 쉽게 적용해 보기 위해서 redisTemplate을 aop로 구현해봤다.
////
////필요에 따라서 ttl을 refresh 하는 로직을 추가하거나 파라미터를 포함하지 않고 특정 key값으로 강제하는 등 원하는 옵션을 구현해도 된다.
////
////간단한 캐시 적용에는 사용하기 좋지만 아무래도 직접 구현하다 보니 기존에 있던 캐시들에 비하면 부족한 부분이 많다. 복잡한 캐싱 로직은 다른 캐시 매니저를 사용하는 것을 고려해보는 것이 좋겠다.
//
//
//
//public class RedisCacheAspect {
//    private RedisTemplate<String, ?> redisTemplate;
//
//    // 여기서 실제 redis에 저장될 키를 만드는 부분이 generateKey 함수이다.
//    //메서드의 파라미터들에 대해서 toString()을 호출해서 key로 만든다.
//    @Around("@annotation(RedisCacheable)")
//    public Object cacheableProcess(ProceedingJoinPoint joinPoint) throws Throwable {
//        // 메소드의 어노테이션 가져오기
//        Signature signature = joinPoint.getSignature();
//        MethodSignature methodSignature = (MethodSignature)signature;
//        RedisCacheable redisCacheable = methodSignature.getMethod().getAnnotation(RedisCacheable.class);
//
//
//        String cacheKey = generateKey(redisCacheable.cacheName, joinPoint, redisCacheable.hasClassAndMethodNamePrefix);
//        Long cacheTTL = redisCacheable.expireSecond;
//        if (redisTemplate.hasKey(cacheKey)){
//            return redisTemplate.opsForValue().get(cacheKey);
//        }
//
//        Object methodReturnValue = joinPoint.proceed();
//
//        if (cacheTTL < 0) {
//            redisTemplate.opsForValue().set(cacheKey, methodReturnValue);
//        } else {
//            redisTemplate.opsForValue().set(cacheKey, methodReturnValue, cacheTTL, TimeUnit.SECONDS);
//        }
//        return methodReturnValue;
//    }
//
//    @Around("@annotation(RedisCacheEvict)")
//    public Object cacheEvictProcess(ProceedingJoinPoint joinPoint) throws Throwable {
//        // 메소드의 어노테이션 가져오기
//        Signature signature = joinPoint.getSignature();
//        MethodSignature methodSignature = (MethodSignature)signature;
//        RedisCacheEvict redisCacheEvict = methodSignature.getMethod().getAnnotation(RedisCacheEvict.class);
//
//        if (redisCacheEvict.clearAll) {
//            val keys = redisTemplate.keys("${redisCacheEvict.cacheName}*");
//            redisTemplate.delete(keys);
//        } else {
//            val cacheKey = generateKey(redisCacheEvict.cacheName, joinPoint, redisCacheEvict.hasClassAndMethodNamePrefix)
//            redisTemplate.delete(cacheKey);
//        }
//        return methodReturnValue;
//    }
//
//    @Around("@annotation(RedisCachePut)")
//    public Object cachePutProcess(ProceedingJoinPoint joinPoint) throws Throwable {
//        // 메소드의 어노테이션 가져오기
//        Signature signature = joinPoint.getSignature();
//        MethodSignature methodSignature = (MethodSignature)signature;
//        RedisCachePut redisCachePut = methodSignature.getMethod().getAnnotation(RedisCachePut.class);
//
//        val cacheKey = generateKey(redisCachePut.cacheName, joinPoint, redisCachePut.hasClassAndMethodNamePrefix)
//        Long cacheTTL = redisCachePut.expireSecond;
//        Object methodReturnValue = joinPoint.proceed();
//        if (cacheTTL < 0) {
//            redisTemplate.opsForValue().set(cacheKey, methodReturnValue);
//        } else {
//            redisTemplate.opsForValue().set(cacheKey, methodReturnValue, cacheTTL, TimeUnit.SECONDS);
//        }
//        return methodReturnValue;
//    }
//
//    private String generateKey(String cacheName, ProceedingJoinPoint joinPoint, Boolean hasClassAndMethodNamePrefix {
//        val generatedKey = StringUtils.arrayToCommaDelimitedString(joinPoint.getArgs());
//        if (hasClassAndMethodNamePrefix) {
//           // joinPoint.getTarget()
//
//            val target = joinPoint.target::class.simpleName
//
//            MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
//            String method = methodSignature.getMethod().getName();
//            return "$cacheName::$target.$method($generatedKey)";
//            }
//            return "$cacheName::($generatedKey)";
//    }
//}
//
////private <T> T getOrProgress(ProceedingJoinPoint pjp, String key, Class<T> returnType, long expireSecond) throws Throwable {
////    T value;
////    Object cacheValue = redisService.get(key);
////    if (cacheValue == null) {
////        value = returnType.cast(pjp.proceed());
////        if (value != null) {
////            if (expireSecond > 0) {
////                redisService.set(key, value, expireSecond, TimeUnit.SECONDS);
////            } else {
////                redisService.set(key, value);
////            }
////        }
////    } else {
////        value = returnType.cast(cacheValue);
////    }
////
////    return value;
////}