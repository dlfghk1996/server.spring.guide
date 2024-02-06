package server.spring.guide.cache.common.config;

import java.io.IOException;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;;

@Slf4j
@Configuration
@EnableCaching
public class EhCacheConfig {
    /* redis를 이미 사용 중이라면 redis가 Ehcache보다 우선순위가 높아
       기본 CacheManger를 RedissonSpringCacheManager로 설정하기 때문에
       따로 Ehcache용 CacheManager를 @Bean으로 등록해주어야 한다.
       Ehcache2 => EhCacheCacheManager에 사용되는 것이다.
       Ehcache3 => JCache/JCacheCacheManger로 등록해줘야한다.
    */
    /*
     * Using Multiple Cache Managers in Spring
     * 1. @Primary
     * 2. 메서드에 cacheManager 명시 => 현재 구현방식
     * 3. Extending CachingConfigurerSupport
     * 4. Using CacheResolver
     * */

    @Bean(name = "ehCacheManager")
    public org.springframework.cache.CacheManager cacheManager() throws IOException {
        CachingProvider cachingProvider = Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");
        CacheManager manager = cachingProvider.getCacheManager(
            new ClassPathResource("/ehcache.xml").getURI(),
            getClass().getClassLoader());

        return new JCacheCacheManager(manager);
    }
}

