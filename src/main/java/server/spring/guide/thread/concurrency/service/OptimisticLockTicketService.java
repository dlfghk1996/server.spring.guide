package server.spring.guide.thread.concurrency.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.spring.guide.common.domain.Ticket;
import server.spring.guide.common.domain.TicketReservationHistory;
import server.spring.guide.common.repository.TicketReservationHistoryRepository;
import server.spring.guide.common.repository.TicketingRepository;
import server.spring.guide.thread.concurrency.annotation.Retry;

@Slf4j
@RequiredArgsConstructor
@Service
public class OptimisticLockTicketService {
    private final TicketingRepository ticketRepository;

    private final TicketReservationHistoryRepository ticketReservationHistoryRepository;

    @Retry
    @Transactional
    public void ticketing(long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new IllegalArgumentException("Ticket Not Found."));
        ticket.increaseReservedAmount();
        int ticketNumber = ticket.getReservedAmount();
        ticketReservationHistoryRepository.save(new TicketReservationHistory(ticket, ticketNumber));
    }
}
