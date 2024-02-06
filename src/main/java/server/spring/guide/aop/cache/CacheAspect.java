//package server.spring.guide.aop.cache;
//
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//import java.util.logging.Logger;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//@Slf4j
//@Aspect
//public class CacheAspect {
//    private Map<String, Object> cache;
//
//    public CacheAspect() {
//        cache = new HashMap<String, Object>();
//    }
//
//    /**
//     * @Cacheable 어노테이션이 붙어 있는 메서드
//     */
//    @Pointcut("execution(@Cacheable * *.*(..))")
//    @SuppressWarnings("unused")
//    private void cache() {
//    }
//
//    @Around("cache()")
//    public Object aroundCachedMethods(ProceedingJoinPoint thisJoinPoint)
//        throws Throwable {
//        log.debug("Execution of Cacheable method catched");
//
//        // generate the key under which cached value is stored
//        // will look like caching.aspectj.Calculator.sum(Integer=1;Integer=2;)
//        StringBuilder keyBuff = new StringBuilder();
//
//        // append name of the class
//        keyBuff.append(thisJoinPoint.getTarget().getClass().getName());
//
//        // append name of the method
//        keyBuff.append(".").append(thisJoinPoint.getSignature().getName());
//
//        keyBuff.append("(");
//        // loop through cacheable method arguments
//        for (final Object arg : thisJoinPoint.getArgs()) {
//            // append argument type and value
//            keyBuff.append(arg.getClass().getSimpleName() + "=" + arg + ";");
//        }
//        keyBuff.append(")");
//        String key = keyBuff.toString();
//
//        log.debug("Key = " + key);
//        Object result = cache.get(key);
//        if (result == null) {
//            log.debug("Result not yet cached. Must be calculated...");
//            result = thisJoinPoint.proceed();
//            log.info("Storing calculated value '" + result + "' to cache");
//            cache.put(key, result);
//        } else {
//            log.debug("Result '" + result + "' was found in cache");
//        }
//
//        return result;
//    }
//
//}
