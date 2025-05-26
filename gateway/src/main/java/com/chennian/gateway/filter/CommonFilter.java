package com.chennian.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CommonFilter implements GlobalFilter {
    private static final Logger log = LoggerFactory.getLogger(CommonFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest().mutate()
                //将获取的真实ip存入header微服务方便获取
                .header("X-Real-IP", exchange.getRequest().getRemoteAddress().getHostString())
                .build();
        log.info(exchange.getRequest().getURI().toString());
        return chain.filter(exchange.mutate().request(request).build());
    }
}
