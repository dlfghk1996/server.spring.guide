package server.spring.guide.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.spring.guide.common.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String name);
}
