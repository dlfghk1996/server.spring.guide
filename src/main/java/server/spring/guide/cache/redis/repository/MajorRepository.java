package server.spring.guide.cache.redis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.spring.guide.cache.redis.domain.Major;

public interface MajorRepository extends JpaRepository<Major, Long> {

}
