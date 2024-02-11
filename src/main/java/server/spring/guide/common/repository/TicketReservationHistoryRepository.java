package server.spring.guide.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.spring.guide.common.domain.TicketReservationHistory;

public interface TicketReservationHistoryRepository extends JpaRepository<TicketReservationHistory, Long> {

    long countByTicketId(Long ticketId);
}
