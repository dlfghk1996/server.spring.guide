package server.spring.guide.cache.common.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
@EnableCaching // 캐싱 이용 명시 및 RedisCacheManager 자동 구성
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

    private final RedisProperties redisProperties;


    // RedisConnectionFactory를 통해 내장 or외부 Redis와 연결
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {

        // RedisNode for 문으로 추가
//        redisProperties.getSentinel().getNodes()
//            .forEach(s ->
//                sentinelConfig.sentinel(s, redisProperties.getPort()));

        //  RedisNode (호스트:포트) 추가
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
            .master(redisProperties.getSentinel().getMaster())
            .sentinel("localhost", 5000)
            .sentinel("localhost", 5001)
            .sentinel("localhost", 5002);

        sentinelConfig.setPassword(redisProperties.getPassword());

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
            // 레디스 명령어 실행&응답 시간 설정
            .commandTimeout(redisCommandTimeout)
            // 데이터 조회 시 데이터를 어떤 노드에서 조회할지 설정
            // (REPLICA_PREFERRED : REPLICA 우선, 조회 불가능시 MASTER에서 조회)
            .readFrom(ReadFrom.REPLICA_PREFERRED)
            .build();
        return new LettuceConnectionFactory(sentinelConfig, clientConfig);
    }

    //   GenericJackson2JsonRedisSerializer<>(Integer.class)
    /* 직렬화 방식
     * 참고 : 직렬화 가능한 Jackson2JsonRedisSerializer 또는 GenericJackson2JsonRedisSerializer로 변경을 하는 것이 좋다.
     * JdkSerializationRedisSerializer: default Serializer, Json 데이터를 직렬화하여 Redis 에 저장이 가능한 형태로 저장, 변경 불가
     * StringRedisSerializer: String 값을 정상적으로 읽어서 저장한다. (binary -> String), UTF-8, Entity or VO cast 불가
     * GenericJackson2JsonRedisSerializer: 객체를 json타입으로 직렬화/역직렬화를 수행한다. (단점: 클래스 타입도 저장됨 => 패키지 이동시  ClassNotFound exception)
     * Jackson2JsonRedisSerializer(classType.class): classType 값을 json 형태로 저장한다. 특정 클래스(classType)에게만 직속되어있다는 단점이 있다.
     */
    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        //redisTemplate.setEnableTransactionSupport(true);

        template.setDefaultSerializer(new StringRedisSerializer());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

       // template.setValueSerializer(new GenericJackson2JsonRedisSerializer(this.objectMapper())); json->object 변환시 설정
        return template;
    }


    // 캐시구성
    @Bean
    @Override
    public RedisCacheManager cacheManager() {
        return RedisCacheManager
            .builder(this.redisConnectionFactory())
            .cacheDefaults(this.cacheConfiguration())
            // Redis key 별 Serialize 생성 시 예
            // .withCacheConfiguration("Major", productConfig)
            // .withCacheConfiguration("User", userConfig)
            .build();
    }

    /**
     * Custom RedisCacheConfiguration : redisCacheManager 직접 옵션 부여 (생략 가능) By adding @EnableCaching.
     * RedisCacheManager with default cache configuration
     */
    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
            .prefixCacheNameWith("test_") // cache prefix
            .entryTtl(Duration.ofMinutes(redisTimeToLive))  // 캐시 만료 시간
            .disableCachingNullValues()  // null 값 캐싱불가 (#result == null 과 함께 사용해야 함)
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(
                new StringRedisSerializer())) // key 직렬화
            .serializeValuesWith(SerializationPair.fromSerializer(
                new StringRedisSerializer())); // value 직력화
    }


    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
                log.info("Failure getting from cache: " + cache.getName() + ", exception: "
                    + exception.toString());
            }

            @Override
            public void handleCachePutError(RuntimeException exception, Cache cache, Object key,
                Object value) {
                log.info("Failure putting into cache: " + cache.getName() + ", exception: "
                    + exception.toString());
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
                log.info("Failure evicting from cache: " + cache.getName() + ", exception: "
                    + exception.toString());
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, Cache cache) {
                log.info("Failure clearing cache: " + cache.getName() + ", exception: "
                    + exception.toString());
            }
        };
    }

    //JSON 직렬화/역직렬화 관련
    // LocalDateTime 포맷 직렬화 오류로 인한 커스텀
    private ObjectMapper objectMapper() {
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator
            .builder()
            .allowIfSubType(Object.class)
            .build();

        return new ObjectMapper()
            .findAndRegisterModules()
            // 콘솔 출력시 개행과 들여쓰기
            .enable(SerializationFeature.INDENT_OUTPUT)
             // 날짜를 문자열로 표시하기 위해서 SerializationFeature.WRITE_DATES_AS_TIMESTAMPS 비활성화
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            // mapping key 없을 떄 발생하는 에러 무시 (=@JsonIgnoreProperties(ignoreUnknown = true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            // LocalDateTime 역,직렬화 =>  with @JsonFormat
            .registerModule(new JavaTimeModule())
            .activateDefaultTyping(ptv, DefaultTyping.EVERYTHING);
    }
}

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
