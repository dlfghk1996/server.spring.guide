package server.spring.guide.cache.redis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;
import server.spring.guide.cache.redis.enums.CachingType;
import server.spring.guide.cache.redis.util.RedisTemplateUtils;
import server.spring.guide.common.domain.User;
import server.spring.guide.common.domain.LikeUp;
import server.spring.guide.common.dto.UserDTO;
import server.spring.guide.common.dto.LikeUpDTO;
import server.spring.guide.common.repository.UserLikeUpRepository;
import server.spring.guide.common.repository.UserRepository;

/**
 * 사용자 프로필 조회수
 * 사용자 프로필 좋아요수
 * */
@Transactional
@RequiredArgsConstructor
@Service
public class LikeUpCacheService {

    private final RedisTemplateUtils redisTemplateUtils;
    private final RankingCacheService rankingCacheService;
    private final ObjectMapper mapper;
    private final UserLikeUpRepository repository;
    private final UserRepository userRepository;

    // TODO 조회수 어뷰징 방지 : IP: 타겟 username => ttl 금일 자정
    public LikeUpDTO addToRedis(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException());

        String field = user.getName()+"::" + userId;
        Double count = redisTemplateUtils.getDataByZSet(CachingType.LIKE.getCode(), field);

        // cache에  값이 없을 경우 DB value +1 한다.
        if(count == null){
            Optional<LikeUp> optUserLikeUp = repository.findById(userId);
            count = optUserLikeUp.isPresent()?optUserLikeUp.get().getCount()+1:(double)1;
            // 좋아요 add
            redisTemplateUtils.setDataByZSet(CachingType.LIKE.getCode(), field,
                count);
        }else{
            count = redisTemplateUtils.incrementByZSet(CachingType.LIKE.getCode(), field,1);
        }

        rankingCacheService.addToRedis(userId);
        return new LikeUpDTO(user.getId(), user.getName(), count.intValue());
    }

    // 전체 조회
    public List<Object> getList() {
        List<TypedTuple<Object>> objectsList = redisTemplateUtils.getListByZSet(CachingType.LIKE.getCode());
        return Arrays.asList(objectsList.toArray());
    }

    //  조회
    public LikeUpDTO getUserLikeUpCntToRedis(Long userId){

        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException());
        String field = user.getName()+"::" + userId;

        // Redis data 조회
        Double countCache = redisTemplateUtils.getDataByZSet(CachingType.LIKE.getCode(), field);

        int count = 0;
        if(countCache == null){
            Optional<LikeUp> optUserLikeUp = repository.findById(userId);
            count = optUserLikeUp.isPresent()? optUserLikeUp.get().getCount():0;
            redisTemplateUtils.setDataByZSet(CachingType.LIKE.getCode(), field, (double) count);
        }else{
            count = countCache.intValue();
        }


        return new LikeUpDTO(user.getId(), user.getName(), count);
    }
}


