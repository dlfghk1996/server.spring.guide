package server.spring.guide.common.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int totalAmount;

    private int reservedAmount = 0;

    // 최신 Version 추적 (동시성이슈 : OPTIMISTIC Lock)
//    @Version
//    private Long version;


    public Ticket(int ticketAmount) {
        this.totalAmount = ticketAmount;
    }

    /***/

    public void increaseReservedAmount() {
        if (reservedAmount >= totalAmount) {
            throw new IllegalArgumentException("Sold out.");
        }
        reservedAmount++;
    }

//    @Version
//    private Integer version;
}
