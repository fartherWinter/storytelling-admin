package com.chennian.storytelling.common.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 多级缓存配置
 * L1: 本地缓存 (ConcurrentHashMap)
 * L2: Redis缓存
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * L1缓存 - 本地缓存管理器
     */
    @Bean("localCacheManager")
    public CacheManager localCacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        cacheManager.setAllowNullValues(false);
        return cacheManager;
    }

    /**
     * L2缓存 - Redis缓存管理器
     */
    @Bean("redisCacheManager")
    @Primary
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 默认缓存配置
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30)) // 默认30分钟过期
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();

        // 不同业务模块的缓存配置
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        // 用户信息缓存 - 1小时
        cacheConfigurations.put("user", defaultConfig.entryTtl(Duration.ofHours(1)));
        
        // 支付信息缓存 - 10分钟
        cacheConfigurations.put("payment", defaultConfig.entryTtl(Duration.ofMinutes(10)));
        
        // 商品信息缓存 - 2小时
        cacheConfigurations.put("product", defaultConfig.entryTtl(Duration.ofHours(2)));
        
        // 库存信息缓存 - 5分钟
        cacheConfigurations.put("inventory", defaultConfig.entryTtl(Duration.ofMinutes(5)));
        
        // 配置信息缓存 - 24小时
        cacheConfigurations.put("config", defaultConfig.entryTtl(Duration.ofHours(24)));
        
        // 热点数据缓存 - 1分钟
        cacheConfigurations.put("hotspot", defaultConfig.entryTtl(Duration.ofMinutes(1)));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

    /**
     * 多级缓存管理器
     */
    @Bean("multiLevelCacheManager")
    public MultiLevelCacheManager multiLevelCacheManager(
            CacheManager localCacheManager,
            CacheManager redisCacheManager) {
        return new MultiLevelCacheManager(localCacheManager, redisCacheManager);
    }
}