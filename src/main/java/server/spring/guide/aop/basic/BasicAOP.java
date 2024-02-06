//package server.spring.guide.aop.basic;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//
///**
// * AOP 적용 방식
// * 컴파일 시점
// * 클래스 로딩 시점
// * 런타임 시점
// * */
//
//@Aspect
//@Component
//@Slf4j
//public class BasicAOP {
//
//    /** @Around
//     * 핵심관심사의 실패여부와 상관없이 전 후로 실행되도록 하는 Advice
//     * 속성값으로 PointCut 전달
//     * */
//    @Around("execution(* server.spring.guide.common.service.UserService.get(..))")
//    public Object doAOP(ProceedingJoinPoint joinPoint) throws Throwable {
//        String methodName = joinPoint.getSignature().toShortString();
//        try {
//            // @Before 수행 : Join Point 실행 이전
//            log.info("[@Before 수행-] {}", methodName);
//            // @Before 종료
//
//            // Target 메서드 호출
//            Object obj = joinPoint.proceed();
//            // Target 메서드 종료
//
//            // @AfterReturning 수행 :조인 포인트가 정상 완료 후 실행 (target 메서드 수행 완료 후 실행)
//            log.info("[@AfterReturning] {}", methodName);
//            // @AfterReturning 종료
//
//            // 값 반환
//            return obj;
//        } catch (Exception e) {
//            // @AfterThrowing 수행 : 메서드가 예외를 던지는 경우
//            log.info("[@AfterThrowing] {}", methodName);
//            throw e;
//            // @AfterThrowing 종료
//        } finally {
//            //@ After 수행 : 조인 포인트의 정상, 예외 동작과 무관하게 실행
//            log.info("[@ After] {}", methodName);
//            //@ After 종료
//        }
//
//    }
//    //@Around() - 메서드 실행 전
//
//    //
//    /* @Pointcut 어노테이션은 애스펙트에서 마치 변수와 같이 재사용 가능한 포인트컷을 정의
//       @PointCut(지시자(접근제어자, 반환형, 패키지를 포함한 클래스경로, 파라미터))
//       within : class의 경로
//    */
//    @Pointcut("within(com.xyz.dao..*)")
//    public void inDataAccessLayer() {}
//
//    @Around("inDataAccessLayer()")
//    public Object measureMethodExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
//        return null;
//    }
//
//
//    // 어노테이션
//    @Around("@annotation(server.spring.guide.basic.annotaion.InitialAnnotaion)")
//    // @Order(1) //JoinPoint에 여러 Advice가 걸려있을시 Advice 수행 순서를 정함.
//    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
//        String methodName = joinPoint.getSignature().toShortString();
//        System.out.println(methodName + "is Start.");
//        Object obj = joinPoint.proceed();
//        return obj;
//    }
//}
//
