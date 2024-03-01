package server.spring.guide.common.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.spring.guide.common.domain.Ticket;
import server.spring.guide.common.domain.TicketReservationHistory;
import server.spring.guide.common.repository.TicketReservationHistoryRepository;
import server.spring.guide.common.repository.TicketingRepository;

@RequiredArgsConstructor
@Service
public class TicketingService {

    private final TicketingRepository ticketRepository;

    private final TicketReservationHistoryRepository ticketReservationHistoryRepository;

    @Transactional
    public Ticket add(int ticketAmount) {
        return ticketRepository.save(new Ticket(ticketAmount));
    }

    @Transactional
    public void ticketing(long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new IllegalArgumentException("Ticket Not Found."));
        ticket.increaseReservedAmount();

        ticketRepository.saveAndFlush(ticket);

        int ticketNumber = ticket.getReservedAmount();
        ticketReservationHistoryRepository.save(new TicketReservationHistory(ticket, ticketNumber));
    }
}
