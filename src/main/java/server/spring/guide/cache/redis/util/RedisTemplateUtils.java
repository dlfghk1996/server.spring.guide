package server.spring.guide.cache.redis.util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;
import server.spring.guide.cache.redis.enums.CachingType;

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
    public void setDataByHash(String hashKey, Long key, Object value){
        // Hash Key1 [(key1:value)(key2:value)]
        // Hash Key2 [(key1:value)(key2:value)]
        redisTemplate.opsForHash().put(key.toString(), hashKey, value);
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
    public void setDataByZSet(String key, String field, Double value){
        boolean result = redisTemplate.opsForZSet().add(key, field, value);
        if(!result){
            throw new RuntimeException();
        }
    }

    /** get List ZSet Type */
    public List<TypedTuple<Object>> getListByZSet(String key){
        Cursor<TypedTuple<Object>> cursor = redisTemplate.opsForZSet()
            .scan(key,ScanOptions.scanOptions().match("*::*")
            .count(10).build());

        List<TypedTuple<Object>> objects = new ArrayList<>();
        while(cursor.hasNext()) {
            objects.add(cursor.next());
        }

        return objects;
    }

    /** get ranking ZSet Type */
    public Long getUserRankingByZSet(String key, String field){
        Long ranking = 0L;

        // 먼저 해당 key 의 점수를 가져온다. (ZSCORE),
        Double score = redisTemplate.opsForZSet().score(key, field);
        // 그 점수의 rank 리스트를 가져온다 (ZREVRANGEBYSCORE),
        // 그 리스트의 첫 key 의 랭킹을 가져온다.
        Set<Object> scoreRankingList = redisTemplate.opsForZSet().reverseRangeByScore(key, score, score, 0, 1);
        for (Object firstRankKey : scoreRankingList) {
            ranking = redisTemplate.opsForZSet().reverseRank(key, firstRankKey);
        }
        //index가 0부터 시작되어서 1 더해준다
        return ranking+1;
    }

    /** get value ZSet Type */
    public Double getDataByZSet(String key, String field){
        return redisTemplate.opsForZSet().score(key, field);
    }

    /** get LinkList ZSet Type */
    public Set<TypedTuple<Object>> getRankingListByZSet(String key, int start, int end){
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }

    /** increment ZSet Type Data */
    public Double incrementByZSet(String key, String userName, double count){
        return redisTemplate.opsForZSet().incrementScore(key, userName, count);
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

    public Set<String> keys(String keyPattern) {
       return redisTemplate.keys(keyPattern);
    }

    /** cahce 유효기간 설정 */
    public void expire(String key, int day, TimeUnit timeUnit) {
        redisTemplate.expire(key, day, timeUnit);
    }


}

