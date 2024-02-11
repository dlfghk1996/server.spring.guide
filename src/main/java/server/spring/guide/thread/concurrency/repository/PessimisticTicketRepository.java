package server.spring.guide.thread.concurrency.repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import server.spring.guide.common.domain.Ticket;


public interface PessimisticTicketRepository extends JpaRepository<Ticket, Long> {

      // 트랜잭션 시작시 Shared&Exclusive Lock (배타적 잠금)적용
      @Lock(LockModeType.PESSIMISTIC_WRITE)
      Optional<Ticket> findById(Long id);

}
