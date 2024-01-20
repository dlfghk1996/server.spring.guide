package server.spring.guide.event.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import server.spring.guide.event.domain.UserPay;

public interface UserPayRepository extends JpaRepository<UserPay, Long> {

    Optional<UserPay> findByUserId(Long userId);
}
