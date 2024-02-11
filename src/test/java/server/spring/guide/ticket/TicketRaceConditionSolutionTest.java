package server.spring.guide.ticket;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import server.spring.guide.common.domain.Ticket;
import server.spring.guide.common.repository.TicketReservationHistoryRepository;
import server.spring.guide.common.service.TicketingService;
import server.spring.guide.thread.concurrency.service.NamedLockTicketFacade;
import server.spring.guide.thread.concurrency.service.OptimisticLockTicketService;
import server.spring.guide.thread.concurrency.service.PessimisticLockTicketService;
import server.spring.guide.thread.concurrency.service.SynchronizedTicketService;

@ActiveProfiles(value = "local")
@SpringBootTest
public class TicketRaceConditionSolutionTest {
    @Autowired
    private TicketingService ticketingService;
    @Autowired
    private TicketReservationHistoryRepository ticketReservationRepository;
    @Autowired
    private SynchronizedTicketService synchronizedTicketService;
    @Autowired
    private PessimisticLockTicketService pessimisticLockTicketService;
    @Autowired
    private  OptimisticLockTicketService optimisticLockTicketService;
    @Autowired
    private NamedLockTicketFacade namedLockUserLikeUpFacade;

    int userCount = 30;

    int ticketAmount = 10;

    Long ticketId;

    private ExecutorService executorService;

    private CountDownLatch countDownLatch;

    @BeforeEach
    public void beforeEach() {
        // then
        Ticket ticket = ticketingService.add(ticketAmount);
        this.ticketId = ticket.getId();

        // threadCount 개수 만큼의 스레드가 있는 스레드 풀 생성
        executorService = Executors.newFixedThreadPool(userCount);
        /*
         * 일정 개수의 쓰레드가 작업이 모두 끝난 뒤 다음으로 진행하거나, 다른 쓰레드를 실행 할 수 있다.
         * 0이라는 정수값이 게이트(Latch)의 역할을 한다. 카운트다운이 되면 게이트(latch)가 열린다.
         * */
        countDownLatch = new CountDownLatch(userCount);
    }

    @Test
    @DisplayName("SYNCHRONIZED_테스트")
    void SYNCHRONIZED를_사용한_예매_테스트() throws InterruptedException {

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // when
        // 30개의 작업을 submit() 메서드로 각각 처리 요청
        IntStream.range(0, userCount).forEach(e -> executorService.submit(() -> {
            try {
                synchronizedTicketService.ticketing(ticketId);
                successCount.incrementAndGet();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                failCount.incrementAndGet();
            } finally {
                countDownLatch.countDown();
            }

        }));

        countDownLatch.await();

        this.printResult(successCount, failCount);
    }

    @Test
    @DisplayName("비관적락 테스트")
    void PESSIMITIC_LOCK를_사용한_예매_테스트() throws InterruptedException {
        // given
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // when
        // 30개의 작업을 submit() 메서드로 각각 처리 요청
        IntStream.range(0, userCount).forEach(e -> executorService.submit(() -> {
            try {
                pessimisticLockTicketService.ticketing(ticketId);
                successCount.incrementAndGet();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                failCount.incrementAndGet();
            } finally {
                countDownLatch.countDown();
            }

        }));

        countDownLatch.await();

        this.printResult(successCount, failCount);
    }


    @Test
    @DisplayName("낙관적락 테스트")
    void OPTIMISTIC_LOCK를_사용한_예매_테스트() throws InterruptedException {
        // given
        int userCount = 3;
        int ticketAmount = 3;
        this.ticketId = ticketingService.add(ticketAmount).getId();
        ExecutorService executorService = Executors.newFixedThreadPool(userCount);
        countDownLatch = new CountDownLatch(userCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // when
        IntStream.range(0, userCount).forEach(e -> executorService.submit(() -> {
                try {
                    optimisticLockTicketService.ticketing(ticketId);
                    successCount.incrementAndGet();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    failCount.incrementAndGet();
                } finally {
                    countDownLatch.countDown();
                }
            }
        ));

        countDownLatch.await();
        this.printResult(successCount, failCount);
    }


    @DisplayName("분산락_테스트")
    @Test
    void NAMED_LOCK를_사용한_예매_테스트() throws InterruptedException {
        // given
        int userCount = 3;
        int ticketAmount = 3;
        this.ticketId = ticketingService.add(ticketAmount).getId();
        ExecutorService executorService = Executors.newFixedThreadPool(userCount);
        countDownLatch = new CountDownLatch(userCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // when
        IntStream.range(0, userCount).forEach(e -> executorService.submit(() -> {
                try {
                    namedLockUserLikeUpFacade.ticketing(ticketId);
                    successCount.incrementAndGet();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    failCount.incrementAndGet();
                } finally {
                    countDownLatch.countDown();
                }
            }
        ));

        countDownLatch.await();

        this.printResult(successCount, failCount);
    }


    public void printResult(AtomicInteger successCount, AtomicInteger failCount){

        // then
        System.out.println("예매 성공 개수 = " + successCount);
        System.out.println("예매 실패 개수 = " + failCount);

        // then
        long reservationCount = ticketReservationRepository.countByTicketId(ticketId);
        System.out.println("### synchronized 처리 이후 수량 ###" + (ticketAmount - reservationCount));
        assertThat(ticketAmount - reservationCount).isZero();
    }
}
