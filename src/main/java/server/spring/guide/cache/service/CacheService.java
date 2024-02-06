package server.spring.guide.cache.service;


import java.awt.print.Book;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;


/** 캐시 추상화 - 어노테이션으로 캐싱 구현 */
/** 공식문서 참고
 * https://docs.spring.io/spring-framework/reference/integration/cache/annotations.html#cache-spel-context*/

// @CacheConfig: 캐시정보를 클래스 단위로 사용하고 관리
@CacheConfig(cacheNames={"userGroupCache"})
@Service
public class CacheService {

    /*
    * 캐시 저장 및 조회
    * 캐시가 미존재 시 캐시 저장, 존재시 결과 반환
    */
    @Cacheable(key="#group",
        // 캐싱 조건
    condition = "#group.length < 2",
        // 캐싱 하지 않는 조건 (null 로 반환되는 데이터)
    unless = "#result == null")
    public List<String> findGroupUserCache(String group) {
        return Arrays.asList("김", "박", "손", "이");
    }

    /*
     * ehcache 사용하여 캐싱
     */
    @Cacheable(value = "group_user", key="#group", cacheManager = "ehCacheManager")
    public List<String> findGroupUserCacheByEhcache(String group) {
        return Arrays.asList("김", "박", "손", "이");
    }


    /*
    * 캐시 저장 or 갱신
    * 캐시의 존재 여부를 떠나서 저장 혹은 갱신 한다.
    * 대상 메서드를 건너뛰게 하는 것이 아니라 항상 메서드가 호출되고 해당 결과가 캐시에 저장되도록 합니다
    */
    @CachePut(key = "#group")
    public List<String> modifyGroupUserCache(String group) {
        System.out.println("modifyGroupUserCache 메서드 호출 ");
        return Arrays.asList("A", "B", "C", "D");
    }


    /* 캐시 삭제 */
    @CacheEvict(key="#group",
        // 캐시 내의 모든 리소스를 삭제할지 여부
        allEntries = true,
        // 메서드 수행 이전 캐시 리소스 삭제 여부
        beforeInvocation = true
    )
    public void deleteGroupUserCache(String group) {
        System.out.println("deleteGroupUserCache 메서드 호출 ");
    }


    /*
    @Caching : 여러 개의 캐싱 동작을 수행해야 할 때 사용
     */
    @Caching(evict = {
        @CacheEvict(key="#group.equals('AA')"),
        @CacheEvict(value = "group_user")})
    public void deleteALl(String group){
    }
}


