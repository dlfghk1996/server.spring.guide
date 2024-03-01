package server.spring.guide.cache.redis.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import server.spring.guide.cache.redis.dto.RankingResponse;
import server.spring.guide.cache.redis.enums.CachingType;
import server.spring.guide.cache.redis.util.RedisTemplateUtils;
import server.spring.guide.common.domain.LikeUp;
import server.spring.guide.common.domain.User;
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
     * 스케줄링을 통해 1시간마다 Redis에 있는 데이터를 List로 바꾼 후,
     * Batch Insert를 통해 하나의 Query를 통해 데이터 MySQL에 Insert.
     */
    @Scheduled(cron = "0 0 0/1 * * *") // 초,분,시,날,달,요일,연도
    public void deleteViewCntCacheFromRedis() {
        // productViewCnt 패턴의 키 조회.
        List<LikeUp> likeUpList = redisTemplateUtils.getListByZSet(CachingType.RANKING.getCode()).stream()
            .map(tuple-> {
                    return new LikeUp(
                        Long.valueOf(((String)tuple.getValue()).split("::")[1]), tuple.getScore());
                }).collect(Collectors.toList());

        repository.saveAll(likeUpList);

        // 이 후에 업데이트 된 데이터베이스 반영을 위해 관련 캐시들을 전부 삭제
        redisTemplateUtils.deleteData(CachingType.RANKING.getCode());
    }



    // Redis에 상위 랭킹
    // 매주 월요일 오전 6시에 상위 DTO로 매핑된 Project를 Redis에 저장한다
    @Scheduled(cron = "0 0 6 ? * MON", zone = "Asia/Seoul")
    public void initializeLikeUpRanking() {
        // Sorted Set으로 관리하는 상위 프로젝트의 Id를 조회한다.
        Set<TypedTuple<Object>> typedTuples = Objects.requireNonNull(
            redisTemplateUtils.getRankingListByZSet(CachingType.RANKING.getCode(), 0, 9));

        List<RankingResponse> rankingResponses = typedTuples.stream()
            .map(tuple-> {
                Long ranking = redisTemplateUtils.getUserRankingByZSet(CachingType.RANKING.getCode(), (String) tuple.getValue());
                Long userId = Long.valueOf(((String) tuple.getValue()).split("::")[1]);
                String userName = ((String) tuple.getValue()).split("::")[0];
                return new RankingResponse(userId, userName, ranking, tuple.getScore().intValue());

            })
            .collect(Collectors.toList());

        rankingResponses.forEach(rankingData ->
            redisTemplateUtils.setDataByHash(CachingType.TOPRANKING.getCode(), rankingData.getUserId(), rankingData));

       // 1주일 단위로 갱신되기 때문에 TTL을 7일로 설정한다.
        redisTemplateUtils.expire(CachingType.TOPRANKING.getCode(), 7, TimeUnit.DAYS);

        // 기존 캐시 데이터 삭제
        redisTemplateUtils.deleteData(CachingType.RANKING.getCode());
    }

}








