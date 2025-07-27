package com.chennian.storytelling.api.config;

import brave.sampler.Sampler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * API 服务链路追踪配置
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Configuration
@Slf4j
public class TracingConfiguration {
    
    @Value("${spring.sleuth.sampler.probability:1.0}")
    private float sampleRate;
    
    /**
     * 配置采样器
     */
    @Bean
    public Sampler alwaysSampler() {
        return Sampler.create(sampleRate);
    }
    

    

}