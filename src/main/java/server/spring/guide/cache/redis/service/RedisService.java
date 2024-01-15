package server.spring.guide.cache.redis.service;


import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import server.spring.guide.cache.redis.controller.dto.MajorDTO;
import server.spring.guide.cache.redis.common.RedisUtils;
import server.spring.guide.cache.redis.domain.MajorCache;
import server.spring.guide.cache.redis.repository.MajorCacheRepository;

//@Import({ CacheConfig.class, MajorService.class}) // Configuration
//@ExtendWith(SpringExtension.class)
//@ImportAutoConfiguration(classes = {
//    CacheAutoConfiguration.class,
//    RedisAutoConfiguration.class
//})
@Service
@RequiredArgsConstructor
public class RedisService {

    // RedisTemplate
    private final RedisTemplate<String, String> redisTemplate;

    // RedisRepository
    private final RedisUtils redisUtil;

    private final MajorCacheRepository majorCacheRepository;


    // RedisRepository : 저장
    public void saveMajorDataWithRepository(MajorDTO request) {
        MajorCache majorCache = new MajorCache();
        majorCache.setName("테스트 학과");
        majorCache.setId("test1");
        majorCacheRepository.save(majorCache);
    }


    // RedisRepository : 조회
    public MajorCache getMajorDataWithRepository(String majorId) {

        return majorCacheRepository.findById(majorId).get();
    }


    // RedisTemplate 사용
    @Cacheable(
        value = "major",
        key = "#majorId",
        //  key = "#root.target + #root.methodName",
        unless="#result.size()==0")
    public String getMajorDataWithTemplate(String majorId) {

        return redisUtil.getData(majorId);
    }

}
