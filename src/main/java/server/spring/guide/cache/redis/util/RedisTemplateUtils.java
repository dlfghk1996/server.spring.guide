package server.spring.guide.cache.redis.util;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

/** RedisTemplate 사용 Utils
 *
 * 레디스 템플릿은 사용하는 자료구조마다 제공하는 메서드가 다르기 때문에
 * 객체를 만들어서 레디스의 자료구조 타입에 맞는 메소드를 사용한다.
 *
 * ex) opsForList(), opsForSet(), opsForZSet()등
 */
@Component
@RequiredArgsConstructor
public class RedisTemplateUtils {
    private final RedisTemplate<String, Object> redisTemplate;

    /** Set String Type */
    public void setDataByString(String key, String value){
        redisTemplate.opsForValue().set(key, value);
    }

    /** Get String Type */
    public String getData(String key){
        return (String) redisTemplate.opsForValue().get(key);
    }

    /** Set Hash Type */
    public void setDataByHash(String key, String hashKey, Long value){
        // Hash Key1 [(key1:value)(key2:value)]
        // Hash Key2 [(key1:value)(key2:value)]
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /** Set all Hash Type */
    public void setAllDataByHash(String key, Map<String, Object> map){
        // putAll(H key, Map<? extends HK,? extends HV> m)
        redisTemplate.opsForHash().putAll(key, map);
    }


    /** Get Hash Type */
    public Object getDataByHash(String key, String hashKey){
        return redisTemplate.opsForHash().get(key, hashKey);
    }


    /** Get all Hash Type Data */
    public Object getListByHash(String hashKey){
        return redisTemplate.opsForHash().entries(hashKey);
    }

    /** increment Hash Type Data */
    public Long incrementByHash(String key, String hashKey, int num){
        return redisTemplate.opsForHash().increment(key,hashKey,num);
    }

    /** delete Hash Type */
    public void deleteDataByHash(String key, String hashKey){
        redisTemplate.opsForHash().delete(hashKey, key);
    }

    /** Set ZSet Type */
    public void setDataByZSet(String key, String userName, Double value){
        redisTemplate.opsForZSet().add(key, userName, value);
    }

    /** get ZSet Type */
    public Long getDataByZSet(String key, String userName){
        Long ranking = 0L;
        Double ranking1 = redisTemplate.opsForZSet().score(key, userName);
        System.out.println(ranking1);
        Set<Object> ranking2 = redisTemplate.opsForZSet().reverseRangeByScore("ranking", ranking, ranking1, 0, 1);
        for (Object s : ranking2) {
            ranking = redisTemplate.opsForZSet().reverseRank(key, s);
        }
        //index가 0부터 시작되어서 1 더해준다
        return ranking+1;
    }

    /** get all ZSet Type */
    public Set<TypedTuple<Object>> getAllDataByZSet(String key){
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, 10);
    }

    /** increment ZSet Type Data */
    public Double incrementByZSet(String key, String userName, double score){
        return redisTemplate.opsForZSet().incrementScore(key, userName, score);
    }

    /** Set List Type */
    public void setDataByList(String key, List<Object> list){
        redisTemplate.opsForList().rightPush(key, list);
    }

    /** Get List Type */
    public List getDataByList(String key){
        Long size = redisTemplate.opsForList().size(key);
        List<Object> list = redisTemplate.opsForList().range(key, 0, size-1);

        return list;
    }

    /** Set Set Type */
    public void setDataBySet(String key, Object value){
        redisTemplate.opsForSet().add(key, value); // 데이터 추가
    }

    /** Get Set Type */
    public Object getDataBySet(String key, String hashKey){

        // members 로 value 조회
        Set<Object> value = redisTemplate.opsForSet().members(key);

        // cursor
        Cursor<Object> cursor = redisTemplate.opsForSet()
            .scan(key, ScanOptions.scanOptions()
                .match("*")
                .count(3)
                .build());

        while(cursor.hasNext()) {
            System.out.println("cursor = " + cursor.next());
        }

        // pop 으로 조회
        return redisTemplate.opsForSet().pop(key);
    }

    /** key 로 데이터 삭제 */
    public void deleteData(String key){
        redisTemplate.delete(key);
    }
}

