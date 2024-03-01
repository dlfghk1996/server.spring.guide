package server.spring.guide.cache.redis.service;


import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.spring.guide.cache.redis.dto.RankingResponse;
import server.spring.guide.cache.redis.enums.CachingType;
import server.spring.guide.cache.redis.util.RedisTemplateUtils;
import server.spring.guide.common.domain.User;
import server.spring.guide.common.repository.UserRepository;

//Graphy 프로젝트를 진행하면서 ‘이번 주 인기 프로젝트’ 기능 구현을 맡았다.
//Redis의 Sorted Set을 사용하여 조회 수를 기준으로 랭킹을 관리하고,
//매주 월요일 오전 6시에 상위 10개 프로젝트를 Redis에 저장한다.
//이번 포스팅은 구현 과정을 기술한다.


/***
 * Sorted set
 * 중복되지 않는 데이터
 * 가중치(score)을 가지고 정렬
 * Score가 같은면 value 에 따라 정렬
 */


@Service
@RequiredArgsConstructor
@Transactional
public class RankingCacheService {

    private final RedisTemplateUtils redisTemplateUtils;
    private final UserRepository userRepository;

    // 전체 랭킹 조회
    public List<RankingResponse> getRankingList() {
        Object rankingList = redisTemplateUtils.getListByHash(CachingType.RANKING.getCode());
        System.out.println(rankingList.toString());
        return null;
    }

    public void addToRedis(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException());
        String field = user.getName() + "::" + userId;
        Double count = redisTemplateUtils.getDataByZSet(CachingType.RANKING.getCode(), field);

        if(count == null){
            redisTemplateUtils.setDataByZSet(CachingType.RANKING.getCode(), field, (double) 1);
        }else{
            redisTemplateUtils.incrementByZSet(CachingType.RANKING.getCode(), field,1);
        }
    }

    // 사용자 랭킹 조회
    public Long getUserRanking(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException());
        String field = user.getName() + "::" + userId;
        return redisTemplateUtils.getUserRankingByZSet(CachingType.RANKING.getCode(), field);

    }

    // 같은 등수 일경우 동점 처리
}

/**
 * 더 고민해봐야 할 것
 * 전체 유저 기준의 등수를 알기 위해선,Redis에 모든 유저에 대한 score정보가 입력되어야 한다.
 * 회원 변경 및 삭제가 일어날때도 Redis value값도 수정해줘야 한다.
 * 동점자 처리 참고(https://developstudy.tistory.com/89)
 * 어뷰징 처리
 * */
