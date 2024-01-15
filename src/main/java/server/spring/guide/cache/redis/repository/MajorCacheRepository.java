package server.spring.guide.cache.redis.repository;

import org.springframework.data.repository.CrudRepository;
import server.spring.guide.cache.redis.domain.MajorCache;

// RedisRepository를 사용 할 경우
public interface MajorCacheRepository extends CrudRepository<MajorCache, String> {

}
