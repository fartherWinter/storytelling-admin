package com.chennian.storytelling.common.annotation;

import com.chennian.storytelling.common.enums.IpLimitEnum;
import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Ip限流
 *
 * @author by chennian
 * @date 2023/7/17 10:06
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IpLimit {
    IpLimitEnum limit();

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
