//package server.spring.guide.thread.async;
//
//
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.stream.IntStream;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.task.TaskRejectedException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.concurrent.ListenableFuture;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import server.spring.guide.common.repository.UserLikeUpRepository;
//
//// 멀티스레드 테스트 환경
////결론 : 사용 방법 (주석 참고)
////방법 1. execute(()->{}) 로 직접 함수 작성 및 실행
////방법 2. implements Runnable, submit() 으로 custom 실행 객체 작성 및 실행
//@Slf4j
//@RestController
//@RequestMapping(value = "/test/sync")
//public class AsyncController {
//    @Autowired
//    private AsyncService serviceTest;
//
//    @Autowired private UserLikeUpRepository repository;
//
//    private ExecutorService executorService;
//    private CountDownLatch countDownLatch;
//
////
//    // return 비동기 test
//    @GetMapping("async/return")
//    public Object returnAsyncTest() {
//
//        log.info("[sync] start");
//        for(int i = 1; i <= 10; i++){
//            ListenableFuture<String> listenableFuture = serviceTest.returnAsync2( i + "" );
//            listenableFuture.addCallback(
//                    result -> System.out.println(result),
//                    error -> System.out.println(error.getMessage()));
//        }
//
//        log.info("[sync] end");
//        return new ResponseEntity(HttpStatus.OK);
//    }
//
////    // 비동기 API TEST1
////    @GetMapping("/callable")
////    public Callable<String> async() throws InterruptedException {
////        log.info("callable");
////        return ()-> {
////            //callable 오브젝트에 담아서 바로 리턴할 수 있도록
////            log.info("async");
////            Thread.sleep(2000);
////            return "hello";
////        };
////    }
////
////    // 비동기 Blocking LoadTest (1초에 100개의 요청을 동시에 날리는 코드)
////    static AtomicInteger counter = new AtomicInteger(0);
////    public static void main(String[] args) throws InterruptedException {
////        ExecutorService es = Executors.newFixedThreadPool(100);
////        RestTemplate rt = new RestTemplate();
////        String url = "http://localhost:8080/callable";
////
////        StopWatch main = new StopWatch();
////        main.start();
////
////        for (int i=0;i<100;i++){
////            es.execute(()->{
////                int idx = counter.addAndGet(1);
////                log.info("Thread" + idx);
////                StopWatch sw = new StopWatch();
////                sw.start();
////                rt.getForObject(url, String.class);
////                sw.stop();
////                log.info("Elapsed: "+idx+" -> "+sw.getTotalTimeSeconds());
////            });
////        }
////        es.shutdown();
////        es.awaitTermination(100, TimeUnit.SECONDS);
////        main.stop();
////        log.info("Total: {}", main.getTotalTimeSeconds());
////    }
////
////
////    // DeferredResult 사용
////    // 요청을 보냈는데 작업을 수행하지 않으면 서블릿 스레드는 대기.
////    // 외부 이벤트 발생 시 다른 스레드에서 setResult 호출하면 그때 결과를 쏘아줌.
////    //    localhost:8080/dr 을 요청하면 응답하지 않고 대기상태
//////    localhost:8080/dr/event 요청 시, OK 출력
//////    localhost:8080/dr 화면이 "Hello"+msg 로 출력
//////    어디에 쓸까?
//////
//////    채팅방에 30명이 요청 날리고, 커넥션 유지한채로 대기
//////    한명이 이벤트 요청
//////    대기하고 있는 30명에게 메시지를 날림
////
////    Queue<DeferredResult<String>> results = new ConcurrentLinkedDeque<>();
////
////    // 아래 코드를 통해 클라이언트가 풀링하는 API
////    @GetMapping("/dr")
////    public DeferredResult<String> callable() throws InterruptedException{
////        log.info("dr");
////        DeferredResult<String> dr = new DeferredResult<>();
////        results.add(dr);
////        return dr;
////    }
////    @GetMapping("/dr/count")
////    public String drcount(){
////        return String.valueOf(results.size());
////    }
////
////    // 외부에서 이벤트 발생시키는 API
////    @GetMapping("/dr/event")
////    public String drevent(String msg){
////        for (DeferredResult<String> dr : results) {
////            dr.setResult("Hello" + msg); // 이 때 결과가 세팅되며, 이 객체를 풀링하고 있는 곳에선 결과를 받을수있음
////            results.remove(dr);
////        }
////        return "OK";
////    }
////
////    // DeferredResult 를 비동기 작업 수행 후 결과를 처리 할때도 사용 할 수 있다.
////    @GetMapping("/dr/api1")
////    public ListenableFuture<String> api1() {
////        ListenableFuture<String> future = serviceTest.returnAsync2();
////        // future.get(); => 이 코드가 호출되는 순간 서블릿 스레드가 점유되어 비동기 의미가 없어짐
////        return future;
////    }
////
////    @GetMapping("/dr/api2")
////    public DeferredResult<String> api() {
////        DeferredResult<String> dr = new DeferredResult<>();
////        ListenableFuture<String> future = serviceTest.returnAsync2();
////        // 콜백을 추가하고 콜백 내에서 DeferredResult에 값을 설정해줌
////        future.addCallback(res -> {
////            dr.setResult(res);
////        }, err -> {
////            dr.setErrorResult(err);
////        });
////        return dr;
////    }
//
//    // ResponseBodyEmitter
//}
