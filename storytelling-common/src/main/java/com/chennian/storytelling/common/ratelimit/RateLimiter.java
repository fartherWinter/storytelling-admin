package com.chennian.storytelling.common.ratelimit;

/**
 * 限流算法
 *
 * @author by chennian
 * @date 2023/6/19 9:31
 */
public interface RateLimiter {
    /**
     * 判断请求是否能够通过
     *
     * @return 能通过返回true否则false
     */
    boolean tryAcquire();
}
