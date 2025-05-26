package com.chennian.storytelling.workflow.config;

import org.flowable.ui.modeler.properties.FlowableModelerAppProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * 工作流设计器配置类
 * 用于配置Flowable流程设计器相关的Bean和资源
 * 
 * @author chennian
 */
@Configuration
public class WorkflowDesignerConfig implements WebMvcConfigurer {

    /**
     * 配置Flowable模型设计器属性
     * 启用REST API并设置设计器相关配置
     */
    @Bean
    @ConditionalOnMissingBean
    public FlowableModelerAppProperties flowableModelerAppProperties() {
        FlowableModelerAppProperties properties = new FlowableModelerAppProperties();
        // 启用REST API
        properties.setRestEnabled(true);
        // 设置设计器资源路径
        properties.setDataSourcePrefix("/workflow/designer");
        return properties;
    }
    
    /**
     * 配置ObjectMapper，用于JSON序列化和反序列化
     * 使用@Primary注解确保此ObjectMapper被优先使用
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 配置日期序列化格式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 注册自定义模块
        SimpleModule module = new SimpleModule();
        objectMapper.registerModule(module);
        return objectMapper;
    }
    
    /**
     * 配置静态资源处理
     * 添加Flowable设计器所需的静态资源路径映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 添加设计器静态资源映射
        registry.addResourceHandler("/workflow/designer/**")
                .addResourceLocations("classpath:/static/workflow/designer/");
    }
}