package com.chennian.storytelling.common.annotation;

import com.chennian.storytelling.common.constant.CacheConstants;
import com.chennian.storytelling.common.enums.LimitType;

import java.lang.annotation.*;

/**
 * 限流注解
 *
 * @author by chennian
 * @date 2023/3/23 16:17
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
    /**
     * 限流key
     */
    public String key() default CacheConstants.RATE_LIMIT_KEY;

    /**
     * 限流时间,单位秒
     */
    public int time() default 60;

    /**
     * 限流次数
     */
    public int count() default 100;

    /**
     * 限流类型
     */
    public LimitType limitType() default LimitType.DEFAULT;
}
