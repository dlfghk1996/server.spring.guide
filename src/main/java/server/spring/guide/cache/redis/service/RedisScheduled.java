//package server.spring.guide.cache.redis.service;
//
//import jakarta.transaction.Transactional;
//import java.util.Iterator;
//import java.util.Set;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//
//@Service
//@RequiredArgsConstructor
//public class RedisScheduled {
//    @Scheduled(cron = "0 0 0 * * *")
//    void remove(String key) {
//        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
//        // hashOperations.;
//        // alreadyVisitQuestionIds.leftPop(KEY);
//
//    }
//
//    // 위의 코드는 Redis에 기록된 정보들을 DB에 업데이트를 진행하면서 데이터의 일관성을 유지하고,
//    // Redis의 저장된 정보들을 초기화 합니다.
//
//    //Reids의 존재하는 Key들을 전부 불러와 Set에 저장합니다.
//    //Iterator를 통해 Key를 하나씩 읽어 각 Key에 저장된 정보들을 DB에 저장합니다.
//    //하지만 만약 Redis가 아직 비어있다면, null값이 들어올 수 있기에 예외처리를 진행해줍니다.
//    //여기서 또 중요한 것은 Redis가 DB의 업데이트를 일정시간마다 실행될 수 있도로 Spring Boot Scheduled를 활용하여 자동화를 시켜놓은 것을 알 수 있습니다.
//    @Scheduled(fixedDelay = 1000L*18L)
//    @Transactional
//    public void deleteViewCntToRedis(){
//        String hashkey = "views";
//        Set<String> Rediskey = redisTemplate.keys("problemId*");
//        Iterator<String> it = Rediskey.iterator();
//        while (it.hasNext()) {
//            String data = it.next();
//            Long problemId = Long.parseLong(data.split("::")[1]);
//            if (redisTemplate.opsForHash().get(data, hashkey) == null){
//                break;
//            }
//            Long viewCnt = Long.parseLong((String.valueOf(redisTemplate.opsForHash().get(data, hashkey))));
//            problemRepositoryImp.addViewCntFromRedis(problemId, viewCnt);
//            redisTemplate.opsForHash().delete(data, hashkey);
//        }
//        System.out.println("views update complete");
//    }
//
//}
//////@Import({ CacheConfig.class, MajorService.class}) // Configuration
//////@ExtendWith(SpringExtension.class)
//////@ImportAutoConfiguration(classes = {
//////    CacheAutoConfiguration.class,
//////    RedisAutoConfiguration.class
//////})
//
//
//
//
//이제 조회를 할때마다 productViewCnt::{productId}의 값이 하나씩 늘어날 것입니다. 이제 조회수는 해결했으니 쌓인 캐시들을 읽어 데이터베이스에 반영해야 합니다. 3분마다 데이터베이스에 반영하도록 하기 위해 스프링 배치 스케쥴러를 이용했습니다.
//
//
//
//메인에 @EnableScheduling을 추가해줘 스케쥴링을 할 수 있게 합니다.
//@Scheduled(cron = "0 0/3 * * * ?")
//    왼쪽부터 초,분,시,날,달,요일,연도를 뜻하며 우리 프로젝트에는 3분마다 메소드 수행을 하도록 설정하였습니다.
//
//
//
//redisTemplate.keys 메소드로 productViewCnt 패턴의 키들을 불러옵니다.
//
//아까 예시에 따르면 "productViewCnt::2", "productViewCnt::4" 입니다. iterator로 키들을 하나씩 부르면서 addViewCntFromRedis라는 메소드로 업데이트를 수행하게 하였습니다. 이 후에 업데이트 된 데이터베이스 반영을 위해 관련 캐시들을 전부 삭제하도록 하였습니다. 이렇게 해야 조회할때 사용된 cacheable의 캐시도 삭제되면서 업데이트된 조회수를 새로 불러올 수 있기 때문입니다.
//
//
//
//@Scheduled(cron = "0 0/3 * * * ?")
//public void deleteViewCntCacheFromRedis() {
//    Set<String> redisKeys = redisTemplate.keys("productViewCnt*");
//    Iterator<String> it = redisKeys.iterator();
//    while (it.hasNext()) {
//        String data = it.next();
//        Long productId = Long.parseLong(data.split("::")[1]);
//        Long viewCnt = Long.parseLong((String) redisTemplate.opsForValue().get(data));
//        productRepository.addViewCntFromRedis(productId,viewCnt);
//        redisTemplate.delete(data);
//        redisTemplate.delete("product::"+productId);
//    }
//}
//
//
//QueryDsl Impl부분에 구현해두었습니다. 같은 id를 찾아 단순 update시키는 로직입니다.
//
//@Override
//public void addViewCntFromRedis(Long productId,Long addCnt) {
//    queryFactory
//        .update(product)
//        .set(product.productViewCnt,addCnt)
//        .where(product.productId.eq(productId))
//        .execute();