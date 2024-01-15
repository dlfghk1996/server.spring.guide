package server.spring.guide.cache.ehcache.common;

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
    // redis를 이미 사용 중이라면 redis가 Ehcache보다 우선순위가 높아
    // 기본 CacheManger를 RedissonSpringCacheManager로 설정한다.
    // 따라서,따로 Ehcache용 CacheManager를 @Bean으로 등록해주어야 한다.
    // 여기서 중요한것은 org.springframework.cache.CacheManger
    // 인터페이스의 구현체로 org.springframework.cache.ehcache.EhCacheCacheManager 가 있는데,
    // 이 클래스는 Ehcache2 버전에 사용되는 것이다.
    // 처음에도 말했듯이 Ehcache3는 JCache의 한 종류로 JCacheCacheManger로 등록해줘야한다.
    @Bean(name = "ehCacheManager")
    public org.springframework.cache.CacheManager cacheManager() throws IOException {
        CachingProvider cachingProvider = Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");
        CacheManager manager = cachingProvider.getCacheManager(
            new ClassPathResource("/ehcache.xml").getURI(),
            getClass().getClassLoader());

        return new JCacheCacheManager(manager);
    }
}


