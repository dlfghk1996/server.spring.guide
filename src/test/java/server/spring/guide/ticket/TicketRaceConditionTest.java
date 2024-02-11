package server.spring.guide.ticket;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import server.spring.guide.common.domain.Ticket;
import server.spring.guide.common.repository.TicketReservationHistoryRepository;
import server.spring.guide.common.service.TicketingService;

@ActiveProfiles(value = "local")
@SpringBootTest
public class TicketRaceConditionTest {
    @Autowired
    private TicketingService ticketingService;

    @Autowired
    private TicketReservationHistoryRepository ticketReservationRepository;

    @Test
    @DisplayName("티켓_순차적_예매_테스트")
    void 티켓_순차적_예매_테스트() {
        // given
        int memberCount = 30;
        int ticketAmount = 10;
        Ticket ticket = ticketingService.add(ticketAmount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // when
        // 10장의 티켓을 30명의 사용자가 예매하는 상황을 가정.
        for (int i = 0; i < memberCount; i++) {
            try {
                ticketingService.ticketing(ticket.getId());
                successCount.incrementAndGet();
            } catch (Exception e) {
                failCount.incrementAndGet();
            }
        }

        System.out.println("successCount = " + successCount);
        System.out.println("failCount = " + failCount);

        // then
        long reservationCount = ticketReservationRepository.count();
        assertThat(reservationCount)
            .isEqualTo(Math.min(memberCount, ticketAmount));
    }

    @Test
    @DisplayName("동시_예매_테스트")
    void 티켓_동시_예매_테스트() throws InterruptedException {
        // given
        int memberCount = 30;
        int ticketAmount = 10;
        Ticket ticket = ticketingService.add(ticketAmount);

        ExecutorService executorService = Executors.newFixedThreadPool(30);
        CountDownLatch latch = new CountDownLatch(memberCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // when
        for (int i = 0; i < memberCount; i++) {
            executorService.submit(() -> {
                try {
                    ticketingService.ticketing(ticket.getId());
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        // 스레드의 모든 작업이 완료된 후 진행되야 하는 쪽에 await() 메서드를 붙이면
        // 그러면 현재 실행중인 스레드는 더이상 진행하지않고 CountDownLatch의 count가 0이 될 때까지 기다린다.
        latch.await();

        // then
        System.out.println("예매 성공 개수 = " + successCount);
        System.out.println("예매 실패 개수 = " + failCount);

        // then
        long reservationCount = ticketReservationRepository.countByTicketId(ticket.getId());
        System.out.println("### synchronized 처리 이후 수량 ###" + (ticketAmount - reservationCount));
        assertThat(ticketAmount - reservationCount).isZero();
    }
}
