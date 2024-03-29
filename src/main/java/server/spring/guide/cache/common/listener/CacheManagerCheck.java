package server.spring.guide.cache.common.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;


/* CommandLineRunner : SpringBoot 구동 시점에 코드 실행
* 현재 적용된 cachemanger 확인
* */
@Component
public class CacheManagerCheck implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CacheManagerCheck.class);

    private final CacheManager cacheManager;

    public CacheManagerCheck(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    // CacheManager 확인
    @Override
    public void run(String... strings) throws Exception {
        logger.info("\n\n" + "=========================================================\n"
            + "Using cache manager: " + this.cacheManager.getClass().getName() + "\n"
            + "=========================================================\n\n");
    }
}