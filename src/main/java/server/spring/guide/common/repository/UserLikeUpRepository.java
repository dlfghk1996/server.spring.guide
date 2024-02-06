package server.spring.guide.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.spring.guide.common.domain.User;
import server.spring.guide.common.domain.UserLikeUp;

public interface UserLikeUpRepository extends JpaRepository<UserLikeUp, Long> {

}
