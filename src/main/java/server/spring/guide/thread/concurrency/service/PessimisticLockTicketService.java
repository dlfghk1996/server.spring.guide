package server.spring.guide.thread.concurrency.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.spring.guide.common.domain.Ticket;
import server.spring.guide.common.domain.TicketReservationHistory;
import server.spring.guide.common.domain.UserLikeUp;
import server.spring.guide.common.repository.TicketReservationHistoryRepository;
import server.spring.guide.common.repository.TicketingRepository;
import server.spring.guide.common.service.UserLikeUpService;
import server.spring.guide.thread.concurrency.repository.PessimisticTicketRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class PessimisticLockTicketService {
    private final PessimisticTicketRepository pessimisticTicketRepository;


    private final TicketReservationHistoryRepository ticketReservationHistoryRepository;

    /* Pessimistic Lock (비관적락) : Shared/Exclusive Lock을 적용
    * 실제 데이터에 Lock 을 걸어서 정합성을 맞춘다.
    * column/row 단계에서 Lock 을 건다.
    * Lock 이 해제되기 전까지 데이터를 가져갈 수 없다.
     */
    @Transactional
    public void ticketing(long ticketId) {
        Ticket ticket = pessimisticTicketRepository.findById(ticketId)
            .orElseThrow(() -> new IllegalArgumentException("Ticket Not Found."));
        ticket.increaseReservedAmount();
        int ticketNumber = ticket.getReservedAmount();
        ticketReservationHistoryRepository.save(new TicketReservationHistory(ticket, ticketNumber));
    }
}
