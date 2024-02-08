package server.spring.guide.cache.redis.service;


import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;
import server.spring.guide.cache.redis.dto.RankingResponse;
import server.spring.guide.cache.redis.enums.CachingType;
import server.spring.guide.common.domain.User;
import server.spring.guide.common.dto.UserLikeUpDTO;
import server.spring.guide.cache.redis.util.RedisTemplateUtils;
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
        System.out.println(
            redisTemplateUtils.getListByHash(CachingType.RANKING.getCode()).toString());

        return (List<RankingResponse>) redisTemplateUtils.getListByHash(CachingType.RANKING.getCode());
    }

    // 사용자 랭킹 조회
    public RankingResponse getUserRanking(Long userId) {
        return null;
    }
}

//따라서 나의 점수와 같은 사람들 중 등수가 제일 높은사람의 등수를 반환할 수 있는 로직을 구성해서 문제를 해결했다.
//회원 변경 및 삭제가 일어날때도 Redis value값을 바꿔줘야한다는 걸 서비스 도중 알게되어서 수정했다.


//다른 해결해야하는 문제는 전체 유저 기준의 등수를 알기 위해선,
// Redis에 모든 유저에 대한 score정보가 입력되어야 한다는 것이다.
// score에 대한 rank는 결국 각 값에 대한 index position이기 때문이다.
//만약 유저의 일부만 캐싱된 상태에서 HTTP 요청을 받게 된다면
// sorted-set에 있는 모든 유저들보다 실제 유저가 더 많기 때문에, 전체 등수 결과에 왜곡이 생긴다.