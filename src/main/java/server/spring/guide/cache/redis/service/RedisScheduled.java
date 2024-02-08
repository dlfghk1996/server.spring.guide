package server.spring.guide.cache.redis.service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.internal.bytebuddy.build.HashCodeAndEqualsPlugin.Sorted;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import server.spring.guide.cache.redis.dto.RankingResponse;
import server.spring.guide.cache.redis.enums.CachingType;
import server.spring.guide.cache.redis.util.RedisTemplateUtils;
import server.spring.guide.common.domain.User;
import server.spring.guide.common.domain.UserLikeUp;
import server.spring.guide.common.repository.UserLikeUpRepository;
import server.spring.guide.common.repository.UserRepository;

/**
 * Redis에 기록된 정보들을 DB에 업데이트를 진행하면서 데이터의 일관성을 유지하고,
 * Redis의 저장된 정보들을 초기화 한다.
 * */

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisScheduled {


    private final RedisTemplateUtils redisTemplateUtils;
    private final UserRepository userRepository;
    private final UserLikeUpRepository repository;

    /**
     * Redis Data -> insert DB
     * 3분마다 메소드 수행
     */
    @Scheduled(cron = "0 0 0/1 * * *") // 초,분,시,날,달,요일,연도
    public void deleteViewCntCacheFromRedis() {
        // productViewCnt 패턴의 키 조회.
        Set<String> redisKeys = redisTemplateUtils.keys("productViewCnt*");
        Iterator<String> it = redisKeys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            Long productId = Long.parseLong(key.split("::")[1]);
            Long viewCnt = Long.parseLong((String) redisTemplateUtils.getData(String.valueOf(productId)));
            // 하지만 만약 Redis가 아직 비어있다면, null값이 들어올 수 있기에 예외처리를 진행해줍니다.
//            if (redisTemplate.opsForHash().get(data, hashkey) == null){
//                break;
//            }
            // DB insert
            // repository.save();
           // repository.addViewCntFromRedis(productId,viewCnt);

            // 이 후에 업데이트 된 데이터베이스 반영을 위해 관련 캐시들을 전부 삭제
            redisTemplateUtils.deleteData(key);
            redisTemplateUtils.deleteData("product::"+productId);
        }
    }

    /**
     * DB data -> Insert Redis
     * 새벽 2시 수행
     */
    @Scheduled(cron = "0 0 2 * * *", zone = "Asia/Seoul")
    public void initializeProjectRanking() {
        //기존 redis caching 데이터 삭제
        Set<String> redisKeys = redisTemplateUtils.keys("productViewCnt*");
        Iterator<String> it = redisKeys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            Long productId = Long.parseLong(key.split("::")[1]);
            redisTemplateUtils.deleteData(key);
            redisTemplateUtils.deleteData("product::"+productId);
        }

        //서버 시작전, redis 에 데이터 적재시키기.
        LocalDateTime current = LocalDateTime.now();
        LocalDateTime cursorDate = current.minusDays(7);

        String cursor = cursorDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS"));
        log.info("7일전 날짜 : {}", cursor);

        //7일전 데이터 전부가져와서, redis에 적재
        //List<UserLikeUp> userLikeUpList = repository.findAllByCreatedAtAfterOrderByCreatedAtDesc(cursor);

        List<UserLikeUp> userLikeUpList = repository.findAll();

        for (UserLikeUp userLikeUp : userLikeUpList) {
            redisTemplateUtils.setDataByZSet(CachingType.LIKE.getCode(),
                "userId::" + userLikeUp.getUserId(), (double) userLikeUp.getCount());
        }
    }

    // Redis에 상위 랭킹
    // 매주 월요일 오전 6시에 상위 DTO로 매핑된 Project를 Redis에 저장한다
    @Scheduled(cron = "0 0 6 ? * MON", zone = "Asia/Seoul")
    public void initializeLikeUpRanking() {
        // Sorted Set으로 관리하는 상위 프로젝트의 Id를 조회한다.
        Set<TypedTuple<Object>> typedTuples = Objects.requireNonNull(
            redisTemplateUtils.getAllDataByZSet(CachingType.LIKE.getCode(), 0, 9));

        List<RankingResponse> rankingResponses = typedTuples.stream()
            .map(tuple-> {
                Long userId = Long.valueOf(((String) tuple.getValue()).split(":")[1]);
                User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException());

                return new RankingResponse(
                    (Long) tuple.getValue(), user.getGender(), user.getName(), "ranking",tuple.getScore());})
            .collect(Collectors.toList());

        rankingResponses.forEach(rankingData ->
            redisTemplateUtils.setDataByHash(CachingType.RANKING.getCode(), rankingData.getUserId(), rankingData));

       // 1주일 단위로 갱신되기 때문에 TTL을 7일로 설정한다.
        redisTemplateUtils.expire(CachingType.RANKING.getCode(), 7, TimeUnit.DAYS);
    }

}








