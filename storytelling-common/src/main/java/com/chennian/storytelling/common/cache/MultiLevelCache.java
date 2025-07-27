package com.chennian.storytelling.common.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 多级缓存实现
 * L1: 本地缓存 (快速访问)
 * L2: Redis缓存 (分布式共享)
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Slf4j
public class MultiLevelCache implements Cache {

    private final String name;
    private final Cache localCache;  // L1缓存
    private final Cache redisCache;  // L2缓存
    
    // 统计信息
    private final AtomicLong l1Hits = new AtomicLong(0);
    private final AtomicLong l1Misses = new AtomicLong(0);
    private final AtomicLong l2Hits = new AtomicLong(0);
    private final AtomicLong l2Misses = new AtomicLong(0);

    public MultiLevelCache(String name, Cache localCache, Cache redisCache) {
        this.name = name;
        this.localCache = localCache;
        this.redisCache = redisCache;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    @Override
    public ValueWrapper get(Object key) {
        // 先从L1缓存获取
        ValueWrapper value = localCache.get(key);
        if (value != null) {
            l1Hits.incrementAndGet();
            log.debug("L1 cache hit for key: {}", key);
            return value;
        }
        
        l1Misses.incrementAndGet();
        
        // L1缓存未命中，从L2缓存获取
        value = redisCache.get(key);
        if (value != null) {
            l2Hits.incrementAndGet();
            log.debug("L2 cache hit for key: {}", key);
            // 将数据同步到L1缓存
            localCache.put(key, value.get());
            return value;
        }
        
        l2Misses.incrementAndGet();
        log.debug("Cache miss for key: {}", key);
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        ValueWrapper wrapper = get(key);
        if (wrapper != null) {
            Object value = wrapper.get();
            if (type.isInstance(value)) {
                return type.cast(value);
            }
        }
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        ValueWrapper wrapper = get(key);
        if (wrapper != null) {
            return (T) wrapper.get();
        }
        
        try {
            T value = valueLoader.call();
            put(key, value);
            return value;
        } catch (Exception e) {
            throw new RuntimeException("Error loading value for key: " + key, e);
        }
    }

    @Override
    public void put(Object key, Object value) {
        if (value == null) {
            return;
        }
        
        // 同时写入L1和L2缓存
        try {
            localCache.put(key, value);
            redisCache.put(key, value);
            log.debug("Put value to both L1 and L2 cache for key: {}", key);
        } catch (Exception e) {
            log.error("Error putting value to cache for key: {}", key, e);
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        ValueWrapper existingValue = get(key);
        if (existingValue == null) {
            put(key, value);
        }
        return existingValue;
    }

    @Override
    public void evict(Object key) {
        try {
            localCache.evict(key);
            redisCache.evict(key);
            log.debug("Evicted key from both L1 and L2 cache: {}", key);
        } catch (Exception e) {
            log.error("Error evicting key from cache: {}", key, e);
        }
    }

    @Override
    public void clear() {
        try {
            localCache.clear();
            redisCache.clear();
            resetStatistics();
            log.info("Cleared both L1 and L2 cache: {}", name);
        } catch (Exception e) {
            log.error("Error clearing cache: {}", name, e);
        }
    }

    /**
     * 预热缓存
     */
    public void warmUp() {
        log.info("Warming up cache: {}", name);
        // 这里可以实现具体的预热逻辑
        // 例如：从数据库加载热点数据到缓存
    }

    /**
     * 获取缓存统计信息
     */
    public MultiLevelCacheManager.CacheStatistics getStatistics() {
        MultiLevelCacheManager.CacheStatistics stats = new MultiLevelCacheManager.CacheStatistics();
        stats.setL1Hits(l1Hits.get());
        stats.setL1Misses(l1Misses.get());
        stats.setL2Hits(l2Hits.get());
        stats.setL2Misses(l2Misses.get());
        stats.setTotalRequests(l1Hits.get() + l1Misses.get());
        return stats;
    }

    /**
     * 重置统计信息
     */
    public void resetStatistics() {
        l1Hits.set(0);
        l1Misses.set(0);
        l2Hits.set(0);
        l2Misses.set(0);
    }

    /**
     * 只从L1缓存获取
     */
    public ValueWrapper getFromL1(Object key) {
        return localCache.get(key);
    }

    /**
     * 只从L2缓存获取
     */
    public ValueWrapper getFromL2(Object key) {
        return redisCache.get(key);
    }

    /**
     * 同步L2缓存数据到L1缓存
     */
    public void syncFromL2ToL1(Object key) {
        ValueWrapper value = redisCache.get(key);
        if (value != null) {
            localCache.put(key, value.get());
        }
    }
}