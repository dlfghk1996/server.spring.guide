package server.spring.guide.cache.redis.domain;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

// Redis Repository를 사용 할 경우 Entity 를 만들어서 사용 할수 있다.

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "major") //설정한 값을 Redis의 key 값 prefix로 사용한다.
public class MajorCache implements Serializable {

    // 키(key) 값이 되며, major:{id} 위치에 auto-increment 된다.
    //@Id 어노테이션을 통해 prefix:구분자 형태(keyspace:@id)로 데이터에 대한 키를 저장하여 각 데이터를 구분
    @Id
    private String id;

    // @Indexed : 값으로 검색을 할 시에 추가한다.
    private String name;

    // @TimeToLive : 만료시간을 설정(초(second) 단위)
    private long ttl;

}
