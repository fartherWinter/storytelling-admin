package com.chennian.storytelling.security.common.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 鉴权配置方法
 *
 * @author by chennian
 * @date 2023/3/18 15:47
 */
@Configuration
public class TokenConfigure implements WebMvcConfigurer {
    /**
     * 打开注解功能鉴权方法
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，打开注解式鉴权功能
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/sys/login", "/sys/captchaImage");
    }

    /**
     * simple模式：token风格替换
     *
     * @return
     */
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }
}
