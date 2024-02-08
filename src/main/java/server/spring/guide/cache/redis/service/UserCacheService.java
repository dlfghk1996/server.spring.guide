package server.spring.guide.cache.redis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.spring.guide.cache.redis.enums.CachingType;
import server.spring.guide.cache.redis.util.RedisTemplateUtils;
import server.spring.guide.common.domain.User;
import server.spring.guide.common.domain.UserLikeUp;
import server.spring.guide.common.dto.UserDTO;
import server.spring.guide.common.repository.UserLikeUpRepository;
import server.spring.guide.common.repository.UserRepository;

/**
 * 사용자 프로필 조회수
 * 사용자 프로필 좋아요수
 * */
@Transactional
@RequiredArgsConstructor
@Service
public class UserCacheService {

    private final RedisTemplateUtils redisTemplateUtils;
    private final ObjectMapper mapper;
    private final UserLikeUpRepository repository;
    private final UserRepository userRepository;

    // 좋아요 - Redis Data Type : Hash Type
    // TODO 중복장지 : IP: 타겟username => ttl 금일 자정
    public void addLikesCntToRedis(Long userId, CachingType cachingType){
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException());

        String key = "userId::" + userId;
        Double count = redisTemplateUtils.getDataByZSet(key, user.getName());

        // cache에  값이 없을 경우 DB에서 값을 가져와서 +1 한다.
        if(count == null){
            UserLikeUp userLikeUp = repository.findById(userId).orElseThrow(()->new RuntimeException());
            // 좋아요 add
            redisTemplateUtils.setDataByZSet(CachingType.LIKE.getCode(), user.getName(),
                (double) userLikeUp.getCount());
        }
        redisTemplateUtils.incrementByZSet(CachingType.LIKE.getCode(),  user.getName(),1);

        System.out.println(count.intValue());
    }

    // 좋아요 조회
    public void getLikesCntToRedis(Long userId, CachingType cachingType){
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException());

        String key = "userId::" + userId;

        // Redis data 조회
        Double count = redisTemplateUtils.getDataByZSet(CachingType.LIKE.getCode(), key);

        if(count == null){
            // db 에서 조회 후 캐싱
            repository.findByUserId(userId);
        }
    }


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


