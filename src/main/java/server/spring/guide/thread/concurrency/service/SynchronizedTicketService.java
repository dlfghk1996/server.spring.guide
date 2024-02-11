package server.spring.guide.thread.concurrency.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.spring.guide.common.domain.Ticket;
import server.spring.guide.common.domain.TicketReservationHistory;
import server.spring.guide.common.domain.UserLikeUp;
import server.spring.guide.common.repository.TicketReservationHistoryRepository;
import server.spring.guide.common.repository.TicketingRepository;
import server.spring.guide.common.repository.UserLikeUpRepository;
import server.spring.guide.common.service.UserLikeUpService;


@RequiredArgsConstructor
@Service
public class SynchronizedTicketService{

    private final TicketingRepository ticketRepository;

    private final TicketReservationHistoryRepository ticketReservationHistoryRepository;

    /** Synchronized 키워드를 적용한 동기화 로직
     * 동기화 메서드 : 메소드 전체 영역이 임계영역
     * 임계영역 : 단 하나의 스레드만 실행할 수 있는 코드 영역
     * 스레드가 객체 내부의 동기화메소드 또는 블록에 들어가면 즉시 객체에 잠금을 걸어 다른 스레드가 임계 영역 코드를 실행하지 못하도록 한다.
     * 다른 스레드는 lock 이 걸린 객체의 모든 동기화 메소드 또는 동기화 블록을 실행할 수 없다
     * **/
    // @Transactional => 불필요한 Race Condition 을 유발시킬 수 있음
    public synchronized void ticketing(long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new IllegalArgumentException("Ticket Not Found."));
        ticket.increaseReservedAmount();

        ticketRepository.saveAndFlush(ticket);

        int ticketNumber = ticket.getReservedAmount();
        ticketReservationHistoryRepository.save(new TicketReservationHistory(ticket, ticketNumber));
    }
}
