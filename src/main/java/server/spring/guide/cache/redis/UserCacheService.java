package server.spring.guide.cache.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.spring.guide.cache.redis.util.RedisTemplateUtils;
import server.spring.guide.common.domain.User;
import server.spring.guide.common.dto.UserDTO;
import server.spring.guide.common.repository.UserRepository;


/* 사용자 정보 캐싱 예제 : userDTO 객체를 캐싱한다.
* Redis Data Type: String
*/
@Transactional
@RequiredArgsConstructor
@Service
public class UserCacheService {

    private final ObjectMapper mapper;
    private final RedisTemplateUtils redisTemplateUtils;
    private final UserRepository userRepository;

    // 회원정보 DTO 저장 - Redis Data Type : String Type
    public UserDTO addUserToRedis(Long userId) throws JsonProcessingException {
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException());

        String json = mapper.writeValueAsString(user);
        String key = "userId::" + user.getId();

        String result = redisTemplateUtils.getData(key);
        if(result == null){
            redisTemplateUtils.setDataByString(key, json);
            return  mapper.readValue(redisTemplateUtils.getData(key),UserDTO.class);
        }
        return  mapper.readValue(result, UserDTO.class);
    }

}
