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
