package server.spring.guide.thread.concurrency.aop;

import jakarta.persistence.OptimisticLockException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.StaleObjectStateException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;


/** 낙관적 잠금 재시도 로직을 구현한 Aspect이다. 최대 1000번까지 0.1초 간격으로 재시도하도록 했다. */
@Order(Ordered.LOWEST_PRECEDENCE-1)
@Aspect
@Component
public class OptimisticLockRetryAspect {
    private static final int MAX_RETRIES = 1000;
    private static final int RETRY_DELAY_MS = 100;


    @Around("@annotation(server.spring.guide.thread.concurrency.annotation.Retry)")
    public Object retryOptimisticLock(ProceedingJoinPoint joinPoint) throws Throwable {
        Exception exceptionHolder = null;

        // 버전 불일치 시 처리
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            try {
                return joinPoint.proceed();
            } catch (OptimisticLockException | ObjectOptimisticLockingFailureException |
                     StaleObjectStateException e) {

                exceptionHolder = e;

                /* 만약 DB 변경 트랜잭션을 만들고자 하는 현재 쓰레드가 이전 Version을 가지고 있다면
                 트랜잭션을 보내지 못하고 1ms 동안 기다린다. */
                Thread.sleep(RETRY_DELAY_MS);
            }
        }
        throw exceptionHolder;
    }
}
