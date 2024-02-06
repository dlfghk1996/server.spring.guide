package server.spring.guide.cache.redis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
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
    //조회수 어뷰징 막기
    //동일한 IP에 대해서는 특정 유저에 대해 하루에 1번만 조회수를 올리고 싶었다.이유는 메인페이지에서 조회수 TOP3인 유저들을 보여주는데, 조회수 어뷰징이 가능하다면 문제가 다분하기 때문이다.
    //Redis의 set자료구조와 ttl을 이용하면 생각보다 구현은 간단했다.
    //IP를 key로 하고 해당 IP가 조회한 username을 set에 넣어는다.
    //해당 IP key에 대해서 ttl을 금일 자정까지 설정하면 된다.
    public void addLikesCntToRedis(Long userId, CachingType cachingType){
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException());

        String key = "userId::" + userId;
        Object object = redisTemplateUtils.getDataByHash(key, "LIKE");

        // cache에  값이 없을 경우 DB에서 값을 가져와서 +1 한다.
        if(object == null){
            UserLikeUp userLikeUp = repository.findById(userId).orElseThrow(()->new RuntimeException());
            // 좋아요 add
            redisTemplateUtils.setDataByHash(key, cachingType.getCode(), Long.valueOf(userLikeUp.getCount()));
            // 랭킹 add
            redisTemplateUtils.setDataByZSet(CachingType.RANKING.getCode(), user.getName(), 120.0);
        }

        redisTemplateUtils.incrementByHash(key, cachingType.getCode(),1);
        redisTemplateUtils.incrementByZSet(CachingType.RANKING.getCode(),  user.getName() ,1);

        System.out.println((Integer) object);
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

//objectMapper.readValue(jsonData, classType)

//@Scheduled(cron = "0 0/3 * * * ?")
//public void deleteViewCntCacheFromRedis() {
//    Set<String> redisKeys = redisTemplate.keys("productViewCnt*");
//    Iterator<String> it = redisKeys.iterator();
//    while (it.hasNext()) {
//        String data = it.next();
//        Long productId = Long.parseLong(data.split("::")[1]);
//        Long viewCnt = Long.parseLong((String) redisTemplate.opsForValue().get(data));
//        productRepository.addViewCntFromRedis(productId,viewCnt);
//        redisTemplate.delete(data);
//        redisTemplate.delete("product::"+productId);
//    }
//}
//
//
//QueryDsl Impl부분에 구현해두었습니다. 같은 id를 찾아 단순 update시키는 로직입니다.
//
//@Override
//public void addViewCntFromRedis(Long productId,Long addCnt) {
//    queryFactory
//        .update(product)
//        .set(product.productViewCnt,addCnt)
//        .where(product.productId.eq(productId))
//        .execute();
//}

//
//처음 생각은 cacheable을 이용해 캐시된 조회수를 조회하고 +1을 한 값을 @cacheput을 이용해 캐시 업데이트를 진행하려고 하였습니다.
//그러나 cacheable을 사용해 캐시 실행을 건너 뛸 수 있고
//cacheput을 실행을 강제해 서로 어긋나 계속해서 cacheable이 안되어 있는 데이터베이스 조회만 수행하였습니다.
//cacheable과 cacheput은 동시에 사용할 수 없었습니다.
//

