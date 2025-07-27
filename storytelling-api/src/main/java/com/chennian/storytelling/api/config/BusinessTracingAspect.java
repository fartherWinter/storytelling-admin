package com.chennian.storytelling.api.config;

import brave.Span;
import brave.Tracer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * 业务链路追踪切面
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Aspect
@Component
@Slf4j
public class BusinessTracingAspect {
    
    @Autowired
    private Tracer tracer;
    
    /**
     * 拦截所有 Controller 方法
     */
    @Around("execution(* com.chennian.storytelling.api.controller..*.*(..))")
    public Object traceController(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        
        Span span = tracer.nextSpan()
                .name(className + "." + methodName)
                .tag("component", "controller")
                .tag("class", className)
                .tag("method", methodName)
                .start();
        
        // 添加请求信息
        addRequestInfo(span);
        
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
            try {
                long startTime = System.currentTimeMillis();
                Object result = joinPoint.proceed();
                long duration = System.currentTimeMillis() - startTime;
                
                span.tag("success", "true")
                    .tag("duration.ms", String.valueOf(duration));
                
                log.debug("Controller method [{}] executed successfully in {}ms", 
                        className + "." + methodName, duration);
                
                return result;
            } catch (Exception e) {
                span.tag("error", "true")
                    .tag("error.message", e.getMessage())
                    .tag("error.class", e.getClass().getSimpleName());
                
                log.error("Controller method [{}] failed: {}", 
                        className + "." + methodName, e.getMessage());
                
                throw e;
            }
        } finally {
            span.finish();
        }
    }
    
    /**
     * 拦截所有 Service 方法
     */
    @Around("execution(* com.chennian.storytelling.service..*.*(..))")
    public Object traceService(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        
        Span span = tracer.nextSpan()
                .name(className + "." + methodName)
                .tag("component", "service")
                .tag("class", className)
                .tag("method", methodName)
                .start();
        
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
            try {
                long startTime = System.currentTimeMillis();
                Object result = joinPoint.proceed();
                long duration = System.currentTimeMillis() - startTime;
                
                span.tag("success", "true")
                    .tag("duration.ms", String.valueOf(duration));
                
                log.debug("Service method [{}] executed successfully in {}ms", 
                        className + "." + methodName, duration);
                
                return result;
            } catch (Exception e) {
                span.tag("error", "true")
                    .tag("error.message", e.getMessage())
                    .tag("error.class", e.getClass().getSimpleName());
                
                log.error("Service method [{}] failed: {}", 
                        className + "." + methodName, e.getMessage());
                
                throw e;
            }
        } finally {
            span.finish();
        }
    }
    
    /**
     * 拦截所有 DAO 方法
     */
    @Around("execution(* com.chennian.storytelling.dao..*.*(..))")
    public Object traceDao(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        
        Span span = tracer.nextSpan()
                .name(className + "." + methodName)
                .tag("component", "dao")
                .tag("class", className)
                .tag("method", methodName)
                .start();
        
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
            try {
                long startTime = System.currentTimeMillis();
                Object result = joinPoint.proceed();
                long duration = System.currentTimeMillis() - startTime;
                
                span.tag("success", "true")
                    .tag("duration.ms", String.valueOf(duration));
                
                log.debug("DAO method [{}] executed successfully in {}ms", 
                        className + "." + methodName, duration);
                
                return result;
            } catch (Exception e) {
                span.tag("error", "true")
                    .tag("error.message", e.getMessage())
                    .tag("error.class", e.getClass().getSimpleName());
                
                log.error("DAO method [{}] failed: {}", 
                        className + "." + methodName, e.getMessage());
                
                throw e;
            }
        } finally {
            span.finish();
        }
    }
    
    /**
     * 添加请求信息到 Span
     */
    private void addRequestInfo(Span span) {
        try {
            ServletRequestAttributes attributes = 
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                
                span.tag("http.method", request.getMethod())
                    .tag("http.url", request.getRequestURL().toString());
                
                String userAgent = request.getHeader("User-Agent");
                if (userAgent != null) {
                    span.tag("http.user_agent", userAgent);
                }
                
                String clientIp = getClientIp(request);
                span.tag("client.ip", clientIp);
            }
        } catch (Exception e) {
            log.warn("Failed to add request info to span: {}", e.getMessage());
        }
    }
    
    /**
     * 获取客户端真实IP
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}