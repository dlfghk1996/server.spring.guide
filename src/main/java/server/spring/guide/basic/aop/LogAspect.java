//package server.spring.guide.basic.aop;
//
//import com.sun.management.ThreadMXBean;
//import jakarta.servlet.http.HttpServletRequest;
//import java.util.Enumeration;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StopWatch;
//import java.lang.management.ManagementFactory;
//
//// jmx : CPU 사욧시간 -> 비즈니스 메서드의 CPU 사용률
//// Thread Local 변수 -> Transaction이 시작될때부터 끝날때 까지를 측정
//
//@Component
//@Slf4j
//@Aspect
//public class LogAspect {
//
//    //@Around() - 메서드 실행 전
//    //@PointCut : 적용될 메서드 (접근제어자, 반환형, 패키지를 포함한 클래스경로, 파라미터)
//    //within : class의 경로
//    @Around("execution(* server.spring.guide..*.*())")
//    public Object logginAuthController(ProceedingJoinPoint joinPoint) throws Throwable {
//
//        // CPU 시간 측정
//        ThreadMXBean threadMXBean = (ThreadMXBean)ManagementFactory.getThreadMXBean();
//
//        long[] allThreadIds = threadMXBean.getAllThreadIds();
//        for(long id : allThreadIds){
//            System.out.println(threadMXBean.getThreadInfo(id).getThreadName());
//            System.out.println("CPU TIME : " + threadMXBean.getThreadCpuTime(id));
//        }
//
//        Object arg = joinPoint.getArgs()[0];
//        log.info("args ={}",arg);
//
//        // 호출되는 메서드에 대한 정보와 파라미터의 목록을 로그로 기록
//        String className = joinPoint.getSignature().getDeclaringTypeName();
//        String methodName = joinPoint.getSignature().getName();
//        String task = className + "." + methodName;
//
//        System.out.println("[service start] " + task);
//
//        // 해당 클래스 처리 전의 시간
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start(String.valueOf(joinPoint.getSignature()));
//
//        /**
//         * 해당 클래스의 메소드 실행
//         * proceed()를 기준으로 이전 코드는 before, 이후 코드는 after
//         * 비지니스 메소드가 실행한 후의 결과 값 return
//         * **/
//        Object result = joinPoint.proceed(); //
//
//        // 해당 클래스 처리 후의 시간
//        stopWatch.stop();
//
//        long executionTime = stopWatch.getTotalTimeMillis();
//
//        log.debug("[ExecutionTime] " + task + "-->" + executionTime + "(ms)");
//        System.out.println("[ExecutionTime] " + task + "-->" + executionTime + "(ms)");
//
//        // 0.5초 ~ 1초 소요 시에는 log.warn
//        if(executionTime <= 1000 || executionTime >= 500){
//            log.warn("[Warn ExecutionTime] {}.{}  ({}ms)", className, executionTime);
//        }
//
//        // 1초 초과 시에는 log.error
//        if(executionTime > 1000 ){
//            log.error("[Error ExecutionTime] {}.{}  ({}ms)", className, executionTime);
//        }
//
////        - 모든 메소드의 시작과 끝에 로깅 메시지를 추가(DEBUG 레벨).
////        - 메소드를 호출할 때 전달되는 모든 인자를 로깅 메시지로 출력(DEBUG 레벨).
//
//        System.out.println("[service end] " + task);
//
////        추가 : DISK IO 유발 방지
//        return result;
//    }
//
//    // db 소요 시간
//    @Around("execution(* server.spring.guide..*(..))") // 패키지 하위에 모두 적용
//    public Object execut(ProceedingJoinPoint joinPoint) throws Throwable {
//        long start = System.currentTimeMillis();
//        System.out.println("[repository Start] " + joinPoint.toString());
//        try {
//
//            return joinPoint.proceed(); // 다음 로직으로 넘어간다.
//        } finally {
//            long finish = System.currentTimeMillis();
//            long timeMs = finish - start;
//            System.out.println("[repository End] " + joinPoint.toString() + " " + timeMs + "ms");
//            if(timeMs >= 500 ){
//                log.warn("[Warn ExecutionTime] {}.{}  ({}ms)", joinPoint.toString());
//            }
//        }
//    }
//}
