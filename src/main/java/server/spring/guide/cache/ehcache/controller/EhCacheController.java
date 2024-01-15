package server.spring.guide.cache.ehcache.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import server.spring.guide.cache.ehcache.service.CacheService;
import server.spring.guide.common.domain.User;

@RequiredArgsConstructor
@Slf4j
@RestController
public class EhCacheController {

    private final CacheService cacheService;

    @GetMapping("/user/nocache/{name}")
    public User getNoCacheMember(@PathVariable String name){

        long start = System.currentTimeMillis(); // 수행시간 측정
        User user = cacheService.findByNameNoCache(name); // db 조회
        long end = System.currentTimeMillis();

        log.info(name+ "의 NoCache 수행시간 : "+ Long.toString(end-start));

        return user;
    }

    @GetMapping("/user/cache/{name}")
    public String getCacheMember(@PathVariable String name){

        long start = System.currentTimeMillis(); // 수행시간 측정
        String userName = cacheService.findUserCache(name); // db 조회
        long end = System.currentTimeMillis();

        log.info(name+ "의 Cache 수행시간 : "+ Long.toString(end-start));

        return userName;
    }

    @GetMapping("/user/cache/delete/{name}")
    public void deleteCacheMember(@PathVariable String name){

        long start = System.currentTimeMillis(); // 수행시간 측정
        cacheService.deleteUserCache(name); // db 조회
        long end = System.currentTimeMillis();

        log.info(name+ "의 Cache 수행시간 : "+ Long.toString(end-start));
    }
}
