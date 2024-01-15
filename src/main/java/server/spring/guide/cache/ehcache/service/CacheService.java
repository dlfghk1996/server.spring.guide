package server.spring.guide.cache.ehcache.service;


import jakarta.transaction.Transactional;
import java.math.BigDecimal;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import server.spring.guide.common.domain.User;
import server.spring.guide.common.repository.UserRepository;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class CacheService {

    private final UserRepository repository;

    // 캐시의 존재 여부를 떠나서 저장 혹은 갱신
    @CachePut(value = "userCache", key = "#name")
    public User saveUserCache(String name) {
        return repository.findByName(name);
    }

    public User findByNameNoCache(String name) {
        slowQuery(2000);
        return repository.findByName(name);
    }

    // 캐시가 미존재 시 캐시 저장, 존재시 결과 반환
    @Cacheable(value = "userCache", key="#name")
//    key = "#root.methodName")            // method 명을 key로 저장
//    condition = "#name.equals('홍길동')"  // 캐싱할 조건
//    unless = "#name == null")            // 캐싱 하지 않는 조건 (null 로 반환되는 데이터)
    public String findUserCache(String name) {
        // 캐시로 조회하는 로직과 캐시가 없을 대 조회하는 로직들 간의 성능 비교를 하기 위해 slowQuery라는 메소드를 추가
        slowQuery(2000);
        User user = repository.findByName(name);
        // 캐시 삭제 했을 경우 예시
        if(user == null){
            return name;
        }
        return user.getName();
    }

    // 캐시 삭제
    @CacheEvict(value = "userCache", key="#name")
    // allEntrires = true 캐시에 저장된 값 모두 제거
    public void deleteUserCache(String name) {
        log.info(name + "의 Cache Clear!");
    }

    // 빅쿼리를 돌린다는 가정
    private void slowQuery(long seconds) {
        try {
            Thread.sleep(seconds);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}


