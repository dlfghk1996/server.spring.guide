package server.spring.guide.thread.async;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@Service
public class AsyncService {

    // 비동기로 동작
    @Async
    public void testAsync(int i) {
        log.info("async i = " + i);
    }


    // return 을 받는 비동기 동작 (리스너 사용)
    @Async
    public ListenableFuture<String> returnAsync2(String message) {

        for(int i = 1; i <= 3; i++){
            System.out.println(message + "비동기 : " + i);
        }

        log.info("This is return Async");
        return new AsyncResult<>("success" + message);
    }
}
