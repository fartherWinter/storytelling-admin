package com.chennian.gateway.filter;

import brave.Span;
import brave.Tracer;
import brave.http.HttpServerHandler;
import brave.http.HttpTracing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Gateway 链路追踪全局过滤器
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Component
@Slf4j
public class TracingGlobalFilter implements GlobalFilter, Ordered {
    
    @Autowired
    private Tracer tracer;
    
    @Autowired
    private HttpTracing httpTracing;
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        
        // 生成请求ID
        String requestId = UUID.randomUUID().toString();
        
        // 创建 Span
        Span span = tracer.nextSpan()
                .name("gateway-" + request.getMethod().name())
                .tag("http.method", request.getMethod().name())
                .tag("http.url", request.getURI().toString())
                .tag("request.id", requestId)
                .tag("component", "spring-cloud-gateway")
                .start();
        
        // 添加路由信息
        String routeId = exchange.getAttribute("org.springframework.cloud.gateway.support.ServerWebExchangeUtils.gatewayRoute");
        if (routeId != null) {
            span.tag("gateway.route.id", routeId);
        }
        
        // 添加请求头信息
        String userAgent = request.getHeaders().getFirst("User-Agent");
        if (userAgent != null) {
            span.tag("http.user_agent", userAgent);
        }
        
        String clientIp = getClientIp(request);
        span.tag("client.ip", clientIp);
        
        // 记录开始时间
        long startTime = System.currentTimeMillis();
        
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
            log.info("Gateway Request [{}]: {} {} from {}", 
                    requestId, request.getMethod(), request.getURI(), clientIp);
            
            return chain.filter(exchange)
                    .doOnSuccess(aVoid -> {
                        // 请求成功
                        long duration = System.currentTimeMillis() - startTime;
                        span.tag("http.status_code", String.valueOf(response.getStatusCode().value()))
                            .tag("duration.ms", String.valueOf(duration));
                        
                        log.info("Gateway Response [{}]: {} - {}ms", 
                                requestId, response.getStatusCode(), duration);
                        
                        span.end();
                    })
                    .doOnError(throwable -> {
                        // 请求异常
                        long duration = System.currentTimeMillis() - startTime;
                        span.tag("error", "true")
                            .tag("error.message", throwable.getMessage())
                            .tag("duration.ms", String.valueOf(duration));
                        
                        log.error("Gateway Error [{}]: {} - {}ms, error: {}", 
                                requestId, request.getURI(), duration, throwable.getMessage());
                        
                        span.end();
                    })
                    .doFinally(signalType -> {
                        // 确保 Span 被关闭
                        if (!span.isNoop()) {
                            span.end();
                        }
                    });
        }
    }
    
    /**
     * 获取客户端真实IP
     */
    private String getClientIp(ServerHttpRequest request) {
        String xForwardedFor = request.getHeaders().getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeaders().getFirst("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        String remoteAddr = request.getRemoteAddress() != null ? 
                request.getRemoteAddress().getAddress().getHostAddress() : "unknown";
        
        return remoteAddr;
    }
    
    @Override
    public int getOrder() {
        // 设置较高优先级，确保在其他过滤器之前执行
        return -100;
    }
}