//package banking.management.config;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.interceptor.KeyGenerator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import java.time.Duration;
//
//@Configuration
//@EnableCaching
//public class RedisConfig {
//
//    @Bean
//    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(10))
//                .disableCachingNullValues()
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));
//
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(redisCacheConfiguration)
//                .build();
//    }
//
//    @Bean
//    public KeyGenerator keyGenerator() {
//        return (target, method, params) -> {
//            StringBuilder sb = new StringBuilder();
//            sb.append(target.getClass().getSimpleName());
//            sb.append(method.getName());
//            for (Object param : params) {
//                sb.append(param.toString());
//            }
//            return sb.toString();
//        };
//    }
//}
//
