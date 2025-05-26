package com.chennian.storytelling.common.redis;

import lombok.NonNull;

import java.util.concurrent.TimeUnit;

/**
 * @author by chennian
 * @date 2023/7/17 10:27
 */
public interface RedisService {
    /**
     * 限流方法
     *
     * @param key
     * @param expireTime
     * @param max
     * @param timeUnit
     * @param userAgent
     * @return
     */
    boolean overRequestRateLimit(@NonNull String key, final int expireTime, final int max,
                                 @NonNull TimeUnit timeUnit, String userAgent);
}
