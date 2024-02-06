package server.spring.guide.cache.redis.repository;

import org.springframework.data.repository.CrudRepository;
import server.spring.guide.cache.redis.domain.CacheEntity;

// RedisRepository를 사용 할 경우
public interface CacheRepository extends CrudRepository<CacheEntity, String> {

}
