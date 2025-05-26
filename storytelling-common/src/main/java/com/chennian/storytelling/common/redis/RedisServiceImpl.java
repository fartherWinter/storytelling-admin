package com.chennian.storytelling.common.redis;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author by chennian
 * @date 2023/7/17 10:28
 */
@Service
@Slf4j
public class RedisServiceImpl implements RedisService {
    private final RedisCache redisCache;

    public RedisServiceImpl(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    /**
     * 请求限流
     *
     * @param key
     * @param expireTime
     * @param max
     * @param timeUnit
     * @param userAgent
     * @return
     */
    @Override
    public boolean overRequestRateLimit(@NonNull String key, int expireTime, int max, @NonNull TimeUnit timeUnit, String userAgent) {
        //获取当前key数据
        long count = redisCache.increment(key)+1;
        long time = redisCache.getExpire(key);
        if (count == 1 || time == -1) {
            redisCache.expire(key, expireTime, timeUnit);
        }
        log.debug("TH api request limit rate:too many requests: key={}, redis count={}, max count={}, " +
                "expire time= {} s, user-agent={} ", key, count, max, expireTime, userAgent);
        return count > max;
    }
}
