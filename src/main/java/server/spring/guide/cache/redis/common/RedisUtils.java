package server.spring.guide.cache.redis.common;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

// RedisTemplate을 사용 할 경우
/** 레디스 템플릿은 사용하는 자료구조마다 제공하는 메서드가 다르기 때문에
 * 객체를 만들어서 레디스의 자료구조 타입에 맞는 메소드를 사용한다.
 */
// 자료구조별 접근 기능 :  opsForList(), opsForSet(), opsForZSet()등의 Redis의 자료구조별 접근 기능
@Component
public class RedisUtils {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtils(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setData(String key, String value,Long expiredTime){
        // String 자료구조에 'key1'을 key로하는 데이터 set
        redisTemplate.opsForValue().set(key, value, expiredTime, TimeUnit.MILLISECONDS);
    }

    public String getData(String key){
       // redisTemplate.opsForHash().get("key1", "hashKey2"); // Hash 자료구조 데이터 조회
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void deleteData(String key){
        redisTemplate.delete(key);
    }
}

