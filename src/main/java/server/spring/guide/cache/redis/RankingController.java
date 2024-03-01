package server.spring.guide.cache.redis;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.spring.guide.cache.redis.dto.RankingResponse;
import server.spring.guide.cache.redis.service.RankingCacheService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ranking")
public class RankingController {
    private final RankingCacheService service;

    @GetMapping("")
    public ResponseEntity<List<RankingResponse>> get(){
        return new ResponseEntity<>(service.getRankingList(), HttpStatus.OK);
    }

    @GetMapping("{userId}")
    public ResponseEntity<Long> getUserRanking(@PathVariable Long userId){
        return new ResponseEntity<>(service.getUserRanking(userId), HttpStatus.OK);
    }

}
