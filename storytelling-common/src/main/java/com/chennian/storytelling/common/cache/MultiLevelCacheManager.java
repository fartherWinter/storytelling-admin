package com.chennian.storytelling.common.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 多级缓存管理器
 * 实现L1本地缓存 + L2 Redis缓存的多级缓存策略
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Slf4j
public class MultiLevelCacheManager implements CacheManager {

    private final CacheManager localCacheManager;
    private final CacheManager redisCacheManager;
    private final ConcurrentMap<String, MultiLevelCache> cacheMap = new ConcurrentHashMap<>();

    public MultiLevelCacheManager(CacheManager localCacheManager, CacheManager redisCacheManager) {
        this.localCacheManager = localCacheManager;
        this.redisCacheManager = redisCacheManager;
    }

    @Override
    public Cache getCache(String name) {
        return cacheMap.computeIfAbsent(name, cacheName -> {
            Cache localCache = localCacheManager.getCache(cacheName);
            Cache redisCache = redisCacheManager.getCache(cacheName);
            return new MultiLevelCache(cacheName, localCache, redisCache);
        });
    }

    @Override
    public Collection<String> getCacheNames() {
        return redisCacheManager.getCacheNames();
    }

    /**
     * 清空所有缓存
     */
    public void clearAll() {
        log.info("Clearing all multi-level caches");
        cacheMap.values().forEach(cache -> {
            try {
                cache.clear();
            } catch (Exception e) {
                log.error("Error clearing cache: {}", cache.getName(), e);
            }
        });
    }

    /**
     * 预热缓存
     */
    public void warmUp(String cacheName) {
        Cache cache = getCache(cacheName);
        if (cache instanceof MultiLevelCache) {
            ((MultiLevelCache) cache).warmUp();
        }
    }

    /**
     * 获取缓存统计信息
     */
    public CacheStatistics getStatistics(String cacheName) {
        Cache cache = getCache(cacheName);
        if (cache instanceof MultiLevelCache) {
            return ((MultiLevelCache) cache).getStatistics();
        }
        return new CacheStatistics();
    }

    /**
     * 缓存统计信息
     */
    public static class CacheStatistics {
        private long l1Hits = 0;
        private long l1Misses = 0;
        private long l2Hits = 0;
        private long l2Misses = 0;
        private long totalRequests = 0;

        public double getL1HitRate() {
            return totalRequests == 0 ? 0.0 : (double) l1Hits / totalRequests;
        }

        public double getL2HitRate() {
            return totalRequests == 0 ? 0.0 : (double) l2Hits / totalRequests;
        }

        public double getTotalHitRate() {
            return totalRequests == 0 ? 0.0 : (double) (l1Hits + l2Hits) / totalRequests;
        }

        // Getters and setters
        public long getL1Hits() { return l1Hits; }
        public void setL1Hits(long l1Hits) { this.l1Hits = l1Hits; }
        public long getL1Misses() { return l1Misses; }
        public void setL1Misses(long l1Misses) { this.l1Misses = l1Misses; }
        public long getL2Hits() { return l2Hits; }
        public void setL2Hits(long l2Hits) { this.l2Hits = l2Hits; }
        public long getL2Misses() { return l2Misses; }
        public void setL2Misses(long l2Misses) { this.l2Misses = l2Misses; }
        public long getTotalRequests() { return totalRequests; }
        public void setTotalRequests(long totalRequests) { this.totalRequests = totalRequests; }
        
        public void incrementL1Hits() { this.l1Hits++; this.totalRequests++; }
        public void incrementL1Misses() { this.l1Misses++; }
        public void incrementL2Hits() { this.l2Hits++; }
        public void incrementL2Misses() { this.l2Misses++; }
    }
}