package server.spring.guide.thread.concurrency.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import server.spring.guide.common.domain.LikeUp;

public interface OptimisticTicketRepository extends JpaRepository<LikeUp, Long> {

    //@Lock(LockModeType.OPTIMISTIC) // -> 생략이 가능한ㄱ?
    Optional<LikeUp> findById(Long id);
}
