package server.spring.guide.cache.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.spring.guide.cache.redis.dto.RankingResponse;
import server.spring.guide.cache.redis.enums.CachingType;
import server.spring.guide.cache.redis.service.RankingCacheService;
import server.spring.guide.cache.redis.service.UserCacheService;
import server.spring.guide.common.dto.UserDTO;

@RequiredArgsConstructor
@RestController
@RequestMapping("cachetest")
public class UserCacheController {

    private final UserCacheService service;
    private final RankingCacheService rankingCacheService;

    // 좋아요 개수 캐싱 - Hash type: Long value insert
    @GetMapping("")
    public void add(){
        service.addLikesCntToRedis(1L, CachingType.LIKE);
    }


    // 사용자 정보 캐싱 - String type: DTO value insert
    @GetMapping("{id}")
    public ResponseEntity<UserDTO> addUserToRedis(@PathVariable Long userId) throws JsonProcessingException {
        return new ResponseEntity<>(service.addUserToRedis(userId), HttpStatus.OK);
    }

    // 좋아요 개수 랭킹 캐싱 - Zset type
    @GetMapping("ranking")
    public ResponseEntity<List<RankingResponse>> addRanking(){
        return new ResponseEntity<>(rankingCacheService.getRankingList(), HttpStatus.OK);
    }

    @GetMapping("/raking/{id}")
    public ResponseEntity<RankingResponse> getRanking(@PathVariable Long userId){
        return new ResponseEntity<>(rankingCacheService.getUserRanking(userId), HttpStatus.OK);
    }

}

//지난 글에서 우리 프로젝트에 개별 상품 조회를 redis로 캐싱 처리했었습니다.
// 캐시 삭제 주기를 6시간으로 많은 시간을 두고 했었는데
// 이렇게 설정하게 되면 그 사이에 조회 수가 올라도
// 이미 cacheable로 캐시에 반영이 되어 있어서 아무리 상품 조회를 많이 해도 조회수가 늘지 않는다는 것입니다.
