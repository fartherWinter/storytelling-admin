package com.chennian.storytelling.common.aspect;

import cn.dev33.satoken.stp.StpUtil;
import com.chennian.storytelling.common.annotation.RateLimit;
import com.chennian.storytelling.common.exception.ServiceException;
import com.chennian.storytelling.common.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * 限流切面
 *
 * @author chennian
 * @date 2024/01/01
 */
@Aspect
@Component
@Slf4j
public class RateLimitAspect {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * Lua脚本，用于原子性地执行限流逻辑
     */
    private static final String RATE_LIMIT_SCRIPT = 
        "local key = KEYS[1]\n" +
        "local count = tonumber(ARGV[1])\n" +
        "local time = tonumber(ARGV[2])\n" +
        "local current = redis.call('get', key)\n" +
        "if current == false then\n" +
        "    redis.call('set', key, 1)\n" +
        "    redis.call('expire', key, time)\n" +
        "    return 1\n" +
        "else\n" +
        "    if tonumber(current) < count then\n" +
        "        return redis.call('incr', key)\n" +
        "    else\n" +
        "        return -1\n" +
        "    end\n" +
        "end";

    @Before("@annotation(rateLimit)")
    public void doBefore(JoinPoint point, RateLimit rateLimit) {
        String key = getCombineKey(rateLimit, point);
        List<String> keys = Collections.singletonList(key);
        
        try {
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
            redisScript.setScriptText(RATE_LIMIT_SCRIPT);
            redisScript.setResultType(Long.class);
            
            Long result = redisTemplate.execute(redisScript, keys, 
                rateLimit.count(), rateLimit.time());
            
            if (result != null && result == -1) {
                log.warn("限流触发，key: {}, 限制: {}次/{}{}", 
                    key, rateLimit.count(), rateLimit.time(), rateLimit.timeUnit());
                throw new ServiceException(rateLimit.message());
            }
            
            log.debug("限流检查通过，key: {}, 当前次数: {}, 限制: {}次/{}{}", 
                key, result, rateLimit.count(), rateLimit.time(), rateLimit.timeUnit());
                
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("限流检查异常", e);
            // 如果Redis异常，不影响正常业务
        }
    }

    /**
     * 获取限流key
     */
    private String getCombineKey(RateLimit rateLimit, JoinPoint point) {
        StringBuilder stringBuilder = new StringBuilder(rateLimit.key());
        
        if (rateLimit.limitType() == RateLimit.LimitType.IP) {
            stringBuilder.append(getIpAddress()).append(":");
        } else if (rateLimit.limitType() == RateLimit.LimitType.USER) {
            try {
                Object loginId = StpUtil.getLoginIdDefaultNull();
                if (loginId != null) {
                    stringBuilder.append(loginId).append(":");
                } else {
                    // 未登录用户使用IP限流
                    stringBuilder.append(getIpAddress()).append(":");
                }
            } catch (Exception e) {
                // 获取用户ID失败，使用IP限流
                stringBuilder.append(getIpAddress()).append(":");
            }
        } else if (rateLimit.limitType() == RateLimit.LimitType.CUSTOM) {
            // 自定义key，使用方法签名
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            Class<?> targetClass = method.getDeclaringClass();
            stringBuilder.append(targetClass.getName()).append(".").append(method.getName()).append(":");
        }
        
        return stringBuilder.toString();
    }

    /**
     * 获取请求IP地址
     */
    private String getIpAddress() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                return IpUtils.getIpAddr(request);
            }
        } catch (Exception e) {
            log.warn("获取IP地址失败", e);
        }
        return "unknown";
    }
}