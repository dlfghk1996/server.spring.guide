package server.spring.guide.cache.redis.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.ReadFrom;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
@EnableCaching
@EnableRedisRepositories // RedisRepository 활성화
@RequiredArgsConstructor
public class RedisConfig implements CachingConfigurer {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.timeout}")
    private Duration redisCommandTimeout;

    @Value("${spring.cache.redis.time-to-live}")
    private long redisTimeToLive;

    @Value("${spring.data.redis.password}")
    private String password;



    private final ObjectMapper objectMapper;
    private final RedisProperties redisProperties;


    // RedisConnectionFactory를 통해 내장 혹은 외부의 Redis를 연결
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {

        System.out.println("master: " + redisProperties.getSentinel().getMaster().toString());
        System.out.println("nodes(host?): " + redisProperties.getSentinel().getNodes().toString());
        System.out.println("port: " + redisProperties.getPort());
        System.out.println("pwd : " +redisProperties.getPassword());


        //  RedisNode (호스트:포트) 추가
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
            .master(redisProperties.getSentinel().getMaster())
            .sentinel("localhost",5000)
            .sentinel("localhost",5001)
            .sentinel("localhost",5002);

        sentinelConfig.setPassword(redisProperties.getPassword());
//        redisProperties.getSentinel().getNodes()
//            .forEach(s ->
//                sentinelConfig.sentinel(s, redisProperties.getPort()));


        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                // 레디스 명령어를 실행하고 응답받는 시간 설정
                .commandTimeout(redisCommandTimeout)
                // 데이터 조회 시 데이터를 어떤 노드에서 조회할지 설정
                // (REPLICA_PREFERRED : REPLICA 우선, 조회 불가능시 MASTER에서 조회)
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .build();
        return new LettuceConnectionFactory(sentinelConfig, clientConfig);
    }



    /* Redis 캐시에서 저장하고 가져올 때 키와 값의 직렬화/역직렬화가 수행되는 방식을 정의
    RedisTemplate: Redis data access code를 간소화 하기 위해 제공되는 클래스이다.
                   주어진 객체들을 자동으로 직렬화/역직렬화 하며 binary 데이터를 Redis에 저장한다.
                   기본설정은 JdkSerializationRedisSerializer 이다.

    StringRedisSerializer: binary 데이터로 저장되기 때문에 이를 String 으로 변환시켜주며(반대로도 가능) UTF-8 인코딩 방식을 사용한다.

    GenericJackson2JsonRedisSerializer: 객체를 json타입으로 직렬화/역직렬화를 수행한다.
    */
    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }


    // 캐시구성
    @Bean
    @Override
    public RedisCacheManager cacheManager() {
        // RedisCacheManagerBuilder:  null 값, 키 접두사 및 바이너리 직렬화를 포함하여 캐시별로 캐싱 동작을 더욱 세부적으로 조정
        return RedisCacheManager
            .builder(this.redisConnectionFactory())
            .cacheDefaults(this.cacheConfiguration())
            // Redis key 별 Serialize 생성 시 예
            // .withCacheConfiguration("Major", productConfig)
            // .withCacheConfiguration("User", userConfig)
            .build();
    }


    /**
     * Spring Boot 가 기본적으로 RedisCacheManager 를 자동 설정해줘서 RedisCacheConfiguration 없어도 사용 가능
     * Bean 을 새로 선언하면 직접 설정한 RedisCacheConfiguration 이 적용됨
     */
    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        // Serializer 변경 (default : JdkSerializationRedisSerializer => Json 데이터를 직렬화하여 Redis에 저장이 가능한 형태로 변경을 할 수 없다.
        // 캐싱할 API의 응답값 데이터 타입이 String 타입이 아니면 Serializer 에러가 발생하므로
        // 직렬화 가능한 Jackson2JsonRedisSerializer 또는 GenericJackson2JsonRedisSerializer로 변경을 합니다.
        return RedisCacheConfiguration.defaultCacheConfig()
            .prefixCacheNameWith("test_") // prefix
            .entryTtl(Duration.ofMinutes(redisTimeToLive))  // 캐시 수명 30분
            .disableCachingNullValues() // 캐싱할 때 null 값을 허용하지 않음 (#result == null 과 함께 사용해야 함)
            // StringRedisSerializer: binary 데이터로 저장되기 때문에 이를 String 으로 변환시켜주며(반대로도 가능) UTF-8 인코딩 방식을 사용한다.
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
    

    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
                log.info("Failure getting from cache: " + cache.getName() + ", exception: " + exception.toString());
            }

            @Override
            public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
                log.info("Failure putting into cache: " + cache.getName() + ", exception: " + exception.toString());
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
                log.info("Failure evicting from cache: " + cache.getName() + ", exception: " + exception.toString());
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, Cache cache) {
                log.info("Failure clearing cache: " + cache.getName() + ", exception: " + exception.toString());
            }
        };
    }

}
//    //JSON 직렬화/역직렬화 관련
//    private ObjectMapper objectMapper() {
//        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator
//            .builder()
//            .allowIfSubType(Object.class)
//            .build();
//
//        return new ObjectMapper()
//            .findAndRegisterModules()
//            .enable(SerializationFeature.INDENT_OUTPUT)
//            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false)
//            .registerModule(new JavaTimeModule())
//            .activateDefaultTyping(ptv, DefaultTyping.NON_FINAL);
//    }

/**
 * 여러 Redis Cache 에 관한 설정을 하고 싶다면 RedisCacheManagerBuilderCustomizer 를 사용할 수 있음
 */
//@Bean
//public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
//    return (builder) -> builder
//        .withCacheConfiguration("cache1",
//            RedisCacheConfiguration.defaultCacheConfig()
//                .computePrefixWith(cacheName -> "prefix::" + cacheName + "::")
//                .entryTtl(Duration.ofSeconds(120))
//                .disableCachingNullValues()
//                .serializeKeysWith(
//                    RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
//                )
//                .serializeValuesWith(
//                    RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())
//                ))
//        .withCacheConfiguration("cache2",
//            RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofHours(2))
//                .disableCachingNullValues());
//}