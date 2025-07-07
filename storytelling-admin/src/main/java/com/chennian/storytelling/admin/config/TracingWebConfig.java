package com.chennian.storytelling.admin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置，注册追踪拦截器
 */
@Configuration
class TracingWebConfig implements WebMvcConfigurer {

    @Autowired
    private ConferenceTracingInterceptor tracingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tracingInterceptor)
                .addPathPatterns("/conference/**")
                .order(1); // 设置较高优先级
    }
}
