package com.chennian.storytelling.admin.config;

import com.chennian.storytelling.admin.controller.workflow.WorkflowPathInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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
                .addPathPatterns("/sys/**/workflow/**") // 匹配所有工作流API路径
                .order(1); // 设置拦截器优先级
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源处理，解决前端路由刷新时的静态资源访问问题
        registry.addResourceHandler("/workflow/**")
                .addResourceLocations("classpath:/static/", "classpath:/public/")
                .setCachePeriod(3600);
        
        // 配置通用静态资源
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600);
                
        // 配置assets资源
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/")
                .setCachePeriod(3600);
    }
}