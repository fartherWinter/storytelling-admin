package com.chennian.storytelling.common.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 限流注解
 * 用于控制API访问频率
 *
 * @author chennian
 * @date 2024/01/01
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /**
     * 限流key的前缀
     */
    String key() default "rate_limit:";

    /**
     * 限流时间窗口，默认60秒
     */
    int time() default 60;

    /**
     * 时间单位，默认秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 限流次数，默认100次
     */
    int count() default 100;

    /**
     * 限流类型
     */
    LimitType limitType() default LimitType.DEFAULT;

    /**
     * 限流失败消息
     */
    String message() default "访问过于频繁，请稍后再试";

    /**
     * 限流类型枚举
     */
    enum LimitType {
        /**
         * 默认策略全局限流
         */
        DEFAULT,
        /**
         * 根据请求者IP进行限流
         */
        IP,
        /**
         * 根据用户ID进行限流
         */
        USER,
        /**
         * 根据自定义key进行限流
         */
        CUSTOM
    }
}