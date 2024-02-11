package server.spring.guide.common.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TicketReservationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 데드락 발생으로 인해 연관관계 제거
//    @ManyToOne
//    @JoinColumn(name = "ticket_id")
    private Long ticketId;

    private int ticketNumber;

    public TicketReservationHistory(Ticket ticket, int ticketNumber) {
        this.ticketId = ticket.getId();
        this.ticketNumber = ticketNumber;
    }
}
