package server.spring.guide.cache.redis.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.spring.guide.cache.redis.controller.dto.MajorDTO;
import server.spring.guide.cache.redis.domain.MajorCache;
import server.spring.guide.cache.redis.service.RedisService;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisController {

    private final RedisService redisService;

    @GetMapping("/template/{id}")
    public ResponseEntity<String> getCacheWithTemplate(@PathVariable String id) {
        String response = redisService.getMajorDataWithTemplate(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public void saveCacheWithRepository(MajorDTO request) {
        redisService.saveMajorDataWithRepository(request);
    }


    @GetMapping("/repository/{id}")
    public ResponseEntity<MajorCache> getCacheWithRepository(@PathVariable String id) {
        return ResponseEntity.ok(redisService.getMajorDataWithRepository(id));
    }


    @GetMapping("/test")
    public ResponseEntity<String> yrdy() {
        System.out.println("hello");
        return ResponseEntity.ok("hello");
    }
}
