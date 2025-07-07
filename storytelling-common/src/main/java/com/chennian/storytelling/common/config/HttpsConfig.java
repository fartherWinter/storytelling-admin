package com.chennian.storytelling.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * HTTPS配置
 *
 * @author chennian
 * @date 2024/01/01
 */
@Configuration
@Slf4j
public class HttpsConfig {

    @Value("${server.ssl.enabled:false}")
    private boolean sslEnabled;

    @Value("${server.port:8080}")
    private int serverPort;

    @Value("${server.ssl.port:8443}")
    private int sslPort;

    @Value("${app.security.force-https:false}")
    private boolean forceHttps;

    /**
     * HTTPS重定向过滤器
     */
    @Bean
    public OncePerRequestFilter httpsRedirectFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, 
                                          HttpServletResponse response, 
                                          FilterChain filterChain) throws ServletException, IOException {
                
                // 如果启用了强制HTTPS且当前不是HTTPS请求
                if (forceHttps && !isSecureRequest(request)) {
                    String httpsUrl = buildHttpsUrl(request);
                    log.info("重定向到HTTPS: {} -> {}", request.getRequestURL(), httpsUrl);
                    response.sendRedirect(httpsUrl);
                    return;
                }

                // 添加安全头
                addSecurityHeaders(response);
                
                filterChain.doFilter(request, response);
            }
        };
    }

    /**
     * Tomcat服务器配置
     */
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return factory -> {
            if (sslEnabled && forceHttps) {
                // 配置HTTP到HTTPS的重定向
                factory.addConnectorCustomizers(connector -> {
                    connector.setScheme("http");
                    connector.setPort(serverPort);
                    connector.setSecure(false);
                    connector.setRedirectPort(sslPort);
                });
                
                log.info("配置HTTP到HTTPS重定向: {}:{} -> {}:{}", 
                    "http", serverPort, "https", sslPort);
            }
        };
    }

    /**
     * 检查是否为安全请求
     */
    private boolean isSecureRequest(HttpServletRequest request) {
        // 检查协议
        if ("https".equalsIgnoreCase(request.getScheme())) {
            return true;
        }
        
        // 检查代理头（用于负载均衡器后的情况）
        String xForwardedProto = request.getHeader("X-Forwarded-Proto");
        if ("https".equalsIgnoreCase(xForwardedProto)) {
            return true;
        }
        
        // 检查其他代理头
        String xForwardedSsl = request.getHeader("X-Forwarded-Ssl");
        if ("on".equalsIgnoreCase(xForwardedSsl)) {
            return true;
        }
        
        return request.isSecure();
    }

    /**
     * 构建HTTPS URL
     */
    private String buildHttpsUrl(HttpServletRequest request) {
        StringBuilder httpsUrl = new StringBuilder();
        httpsUrl.append("https://");
        httpsUrl.append(request.getServerName());
        
        // 如果不是标准HTTPS端口，添加端口号
        if (sslPort != 443) {
            httpsUrl.append(":").append(sslPort);
        }
        
        httpsUrl.append(request.getRequestURI());
        
        if (request.getQueryString() != null) {
            httpsUrl.append("?").append(request.getQueryString());
        }
        
        return httpsUrl.toString();
    }

    /**
     * 添加安全响应头
     */
    private void addSecurityHeaders(HttpServletResponse response) {
        // 强制使用HTTPS（HSTS）
        if (forceHttps) {
            response.setHeader("Strict-Transport-Security", 
                "max-age=31536000; includeSubDomains; preload");
        }
        
        // 防止点击劫持
        response.setHeader("X-Frame-Options", "DENY");
        
        // 防止MIME类型嗅探
        response.setHeader("X-Content-Type-Options", "nosniff");
        
        // XSS保护
        response.setHeader("X-XSS-Protection", "1; mode=block");
        
        // 内容安全策略
        response.setHeader("Content-Security-Policy", 
            "default-src 'self'; " +
            "script-src 'self' 'unsafe-inline' 'unsafe-eval'; " +
            "style-src 'self' 'unsafe-inline'; " +
            "img-src 'self' data: https:; " +
            "font-src 'self' https:; " +
            "connect-src 'self'; " +
            "frame-ancestors 'none'");
        
        // 引用策略
        response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        
        // 权限策略
        response.setHeader("Permissions-Policy", 
            "camera=(), microphone=(), geolocation=(), payment=()");
    }
}