//package server.spring.guide.thread;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.stream.IntStream;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import server.spring.guide.common.repository.UserLikeUpRepository;
//import server.spring.guide.thread.concurrency.service.impl.UserLikeUpSynchronizedServiceImpl;
//
//public class RedisConcurrencyTest {
//    @Autowired
//    private UserLikeUpSynchronizedServiceImpl service;
//    //
////    @Autowired private OptimisticLockStockFacade stockOptimisticLockFacade;
////
////    @Autowired private NamedLockStockFacade namedLockStockFacade;
//    @Autowired private UserLikeUpRepository repository;
//
//    private final int threadCount = 30;
//    private final long id = 2L;
//    private final int count = 1;
//    private final int initQuantity = 30;
//
//    private ExecutorService executorService;
//    private CountDownLatch countDownLatch;
//
//
//    @DisplayName("redis lettuce lock 을 사용한 재고 감소 - 동시에 1000개 테스트 | 49.581s 소요")
//    // Redis를 사용하면 트랜잭션에 따라 대응되는 현재 트랜잭션 풀 세션 관리를 하지 않아도 되므로 구현이 편리하다.
//    // Spin Lock 방식이므로 부하를 줄 수 있어서 thread busy waiting을 통하여 요청 간의 시간을 주어야 한다.
//    @Test
//    void LETTUCE_LOCK을_사용한_재고_감소() throws InterruptedException {
//        // given
//
//        // when
//        IntStream.range(0, threadCount).forEach(e -> executorService.submit(() -> {
//                try {
//                    lettuceLockStockFacade.decrease(productId, quantity);
//                } catch (InterruptedException ex) {
//                    throw new RuntimeException(ex);
//                } finally {
//                    countDownLatch.countDown();
//                }
//            }
//        ));
//
//        countDownLatch.await();
//
//        // then
//        final Long afterQuantity = stockRepository.getByProductId(productId).getQuantity();
//        System.out.println("### LETTUCE LOCK 동시성 처리 이후 수량 ###" + afterQuantity);
//        assertThat(afterQuantity).isZero();
//    }
//
//    @DisplayName("redis reddison lock 을 사용한 재고 감소 - 동시에 1000개 테스트 | 17.23s 소요")
//    @Test
//    void REDISSON_LOCK을_사용한_재고_감소() throws InterruptedException {
//        // given
//
//        // when
//        IntStream.range(0, threadCount).forEach(e -> executorService.submit(() -> {
//                try {
//                    redissonLockStockFacade.decrease(productId, quantity);
//                } catch (InterruptedException ex) {
//                    throw new RuntimeException(ex);
//                } finally {
//                    countDownLatch.countDown();
//                }
//            }
//        ));
//
//        countDownLatch.await();
//
//        // then
//        final Long afterQuantity = stockRepository.getByProductId(productId).getQuantity();
//        System.out.println("### REDDISON LOCK 동시성 처리 이후 수량 ###" + afterQuantity);
//        assertThat(afterQuantity).isZero();
//    }
//}
