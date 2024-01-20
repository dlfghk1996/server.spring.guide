package server.spring.guide.event.repository;

import java.time.Month;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import server.spring.guide.event.domain.AttendanceCycle;

public interface AttendanceCycleRepository extends JpaRepository<AttendanceCycle, Long>{

  //  @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<AttendanceCycle> findByUserIdAndMonthAndStep(Long userId, Month month, int step);
}
