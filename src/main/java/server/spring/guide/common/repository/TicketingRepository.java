package server.spring.guide.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.spring.guide.common.domain.Ticket;

public interface TicketingRepository extends JpaRepository<Ticket, Long> {

}
