package server.spring.guide.thread.async;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ThreadConfig  {

//     ThreadPoolTaskExecutor를 설정하여 쓰레드 풀을 사용 (코어 기준)
//     @Async 어노테이션은 요청마다 스레드를 생성하기 때문에 실무에서 그냥 사용했다간 큰일난다.
//     스레드 풀에 갯수를 제한하여 관리할 수 있다.
    @Bean
    public Executor asyncThreadTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        // 기본 실행 대기하는 Thread의 수
        threadPoolTaskExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        // 동시 동작하는 최대 Thread의 수 (=생성할 수 있는 thread의 총 개수)
        threadPoolTaskExecutor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 2);
        threadPoolTaskExecutor.setThreadNamePrefix("async-thread-pool");
        // MaxPoolSize 초과 요청에서 Thread 생성 요청시, 해당 요청을 Queue에 저장하는데 이때 최대 수용 가능한 Queue의 수
        // Queue에 저장되어있다가 Thread에 자리가 생기면 하나씩 빠져나가 동작한다.
        threadPoolTaskExecutor.setQueueCapacity(50);
        return threadPoolTaskExecutor;
    }
}

// TODO tomcat thread 랑 다른게 뭘까?
// 스레드풀 기본 플로우
/**
 * 1. 첫 작업이 들어오면, core size만큼의 스레드를 생성합니다.
 * 2. 유저 요청(Connection, Server socket에서 accept한 소캣 객체)이 들어올 때마다 작업 큐(queue)에 담아둡니다.
 * 3. core size의 스레드 중, 유휴상태(idle)인 스레드가 있다면 작업 큐에서 작업을 꺼내 스레드에 작업을 할당하여 작업을 처리합니다.
 * 3-1. 만약 유휴상태인 스레드가 없다면, 작업은 작업 큐에서 대기합니다.
 * 3-2. 그 상태가 지속되어 작업 큐가 꽉 찬다면, 스레드를 새로 생성합니다.
 * 3-3. 3번과정을 반복하다 스레드 최대 사이즈 에 도달하고 작업큐도 꽉 차게 되면, 추가 요청에 대해선 connection-refused 오류를 반환합니다.
 * 태스크가 완료되면 스레드는 다시 유휴상태로 돌아갑니다.
 * 4-1. 작업큐가 비어있고 core size이상의 스레드가 생성되어있다면 스레드를 destory합니다.

 * **/