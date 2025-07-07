package com.chennian.storytelling.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

/**
 * 跨域配置
 *
 * @author chennian
 * @date 2024/01/01
 */
@Configuration
@Slf4j
public class CorsConfig {

    @Value("${app.security.cors-enabled:true}")
    private boolean corsEnabled;

    @Value("${app.security.allowed-origins:http://localhost:3000,https://localhost:3000}")
    private String[] allowedOrigins;

    /**
     * CORS配置源
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        if (corsEnabled) {
            // 允许的来源
            if (allowedOrigins.length > 0) {
                configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
            } else {
                // 开发环境可以允许所有来源，生产环境应该明确指定
                configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
            }
            
            // 允许的HTTP方法
            configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"
            ));
            
            // 允许的请求头
            configuration.setAllowedHeaders(Arrays.asList(
                "Origin", "Content-Type", "Accept", "Authorization", 
                "Access-Control-Request-Method", "Access-Control-Request-Headers",
                "X-Requested-With", "token", "X-Token", "Cache-Control"
            ));
            
            // 暴露的响应头
            configuration.setExposedHeaders(Arrays.asList(
                "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials",
                "Access-Control-Allow-Headers", "Access-Control-Max-Age",
                "Access-Control-Allow-Methods", "token", "X-Token"
            ));
            
            // 是否允许发送Cookie
            configuration.setAllowCredentials(true);
            
            // 预检请求的缓存时间（秒）
            configuration.setMaxAge(3600L);
            
            log.info("CORS配置已启用，允许的来源: {}", Arrays.toString(allowedOrigins));
        } else {
            log.info("CORS配置已禁用");
        }
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * CORS过滤器
     */
    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }
}