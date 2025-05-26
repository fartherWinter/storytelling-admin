package com.chennian.storytelling.common.aspect;


import com.chennian.storytelling.common.annotation.IpLimit;
import com.chennian.storytelling.common.enums.IpLimitEnum;
import com.chennian.storytelling.common.exception.StorytellingBindException;
import com.chennian.storytelling.common.redis.RedisService;
import com.chennian.storytelling.common.utils.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * ip限流aop
 *
 * @author by chennian
 * @date 2023/7/17 10:12
 */

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IpLimitAspect {
    private final RedisService redisService;

    @Around("@annotation(com.chennian.storytelling.common.annotation.IpLimit)")
    public Object requestRateLimit(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = ServletUtils.getRequest();
        String requestIp = request.getHeader("X-Real-IP");
        String userAgent = request.getHeader("user-agent");
        String requestUri = request.getRequestURI();
        final Method method = ((MethodSignature) point.getSignature()).getMethod();
        IpLimit ipLimit = method.getAnnotation(IpLimit.class);
        // Gets annotation params
        IpLimitEnum ipLimitEnum = ipLimit.limit();
        TimeUnit timeUnit = ipLimit.timeUnit();
        int[] limitParams = getLimitParams(ipLimitEnum);
        // Generates key
        String key = String.format("th_api_request_limit_rate_%s_%s_%s", requestIp, method.getName(), requestUri);
        // Checks
        boolean over = redisService.overRequestRateLimit(key, limitParams[0], limitParams[1], timeUnit, userAgent);
        if(over){
            throw new StorytellingBindException("点击过于频繁，请稍后再试。");
        }
        return point.proceed();
    }

    /***
     * In the returned array,
     *
     * @return ↓
     *        elements[0] is the time limit,
     *        elements[1] is the number of times that can be requested within the time limit.
     */
    private static int[] getLimitParams(IpLimitEnum ipLimitEnum) {
        String limit = ipLimitEnum.limit();
        int[] result = new int[2];
        String[] limits = limit.split("/");
        result[0] = Integer.parseInt(limits[0]);
        result[1] = Integer.parseInt(limits[1]);
        return result;
    }
}
