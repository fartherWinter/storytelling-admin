package com.chennian.storytelling.admin.config;

import com.chennian.storytelling.admin.controller.workflow.WorkflowPathInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 工作流Web配置类
 * 注册工作流相关的拦截器和配置
 * 
 * @author chennian
 */
@Configuration
public class WorkflowWebConfig implements WebMvcConfigurer {

    @Autowired
    private WorkflowPathInterceptor workflowPathInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册工作流路径拦截器
        registry.addInterceptor(workflowPathInterceptor)
                .addPathPatterns("/api/**/workflow/**") // 匹配所有工作流API路径
                .order(1); // 设置拦截器优先级
    }
}