package server.spring.guide.aop.cache;

// AOP 로 캐시의 만료시간 관리

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import lombok.val;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import server.spring.guide.aop.cache.enums.RedisCacheEvict;
import server.spring.guide.aop.cache.enums.RedisCachePut;
import server.spring.guide.aop.cache.enums.RedisCacheable;



//캐시의 ttl 을 aop 로 관리
public class RedisCacheAspect {
    private RedisTemplate<String, Object> redisTemplate;

    // 여기서 실제 redis에 저장될 키를 만드는 부분이 generateKey 함수이다.
    //메서드의 파라미터들에 대해서 toString()을 호출해서 key로 만든다.
    @Around("@annotation(RedisCacheable)")
    public Object cacheableProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        // 메소드의 어노테이션 가져오기
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        RedisCacheable redisCacheable = methodSignature.getMethod().getAnnotation(RedisCacheable.class);

        String cacheKey = generateKey(redisCacheable.cacheName, joinPoint, redisCacheable.hasClassAndMethodNamePrefix);
        Long cacheTTL = redisCacheable.expireSecond;
        if (redisTemplate.hasKey(cacheKey)){
            return redisTemplate.opsForValue().get(cacheKey);
        }

        Object methodReturnValue = joinPoint.proceed();

        if (cacheTTL < 0) {
            redisTemplate.opsForValue().set(cacheKey, methodReturnValue);
        } else {
            redisTemplate.opsForValue().set(cacheKey, methodReturnValue, cacheTTL, TimeUnit.SECONDS);
        }
        return methodReturnValue;
    }

    @Around("@annotation(RedisCacheEvict)")
    public Object cacheEvictProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        // 메소드의 어노테이션 가져오기
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        RedisCacheEvict redisCacheEvict = methodSignature.getMethod().getAnnotation(RedisCacheEvict.class);
        Object methodReturnValue = joinPoint.proceed();
        if (redisCacheEvict.clearAll) {
            val keys = redisTemplate.keys("${redisCacheEvict.cacheName}*");
            redisTemplate.delete(keys);
        } else {
            val cacheKey = generateKey(redisCacheEvict.cacheName, joinPoint, redisCacheEvict.hasClassAndMethodNamePrefix);
            redisTemplate.delete(cacheKey);
        }
        return methodReturnValue;
    }

    @Around("@annotation(RedisCachePut)")
    public Object cachePutProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        // 메소드의 어노테이션 가져오기
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        RedisCachePut redisCachePut = methodSignature.getMethod().getAnnotation(RedisCachePut.class);

        val cacheKey = generateKey(redisCachePut.cacheName, joinPoint, redisCachePut.hasClassAndMethodNamePrefix);
        Long cacheTTL = redisCachePut.expireSecond;
        Object methodReturnValue = joinPoint.proceed();

        if (cacheTTL < 0) {
            redisTemplate.opsForValue().set(cacheKey, methodReturnValue);
        } else {
            redisTemplate.opsForValue().set(cacheKey, methodReturnValue, cacheTTL, TimeUnit.SECONDS);
        }
        return methodReturnValue;
    }

    private String generateKey(String cacheName, ProceedingJoinPoint joinPoint, Boolean hasClassAndMethodNamePrefix) {
        val generatedKey = StringUtils.arrayToCommaDelimitedString(joinPoint.getArgs());
        if (hasClassAndMethodNamePrefix) {
            val object = joinPoint.getTarget();

            MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
            String method = methodSignature.getMethod().getName();
            return "$cacheName::$target.$method($generatedKey)";
            }
            return "$cacheName::($generatedKey)";
    }
}