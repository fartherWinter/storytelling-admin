package com.chennian.storytelling.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Storytelling API Gateway 应用启动类
 * 作为微服务架构的API网关，负责路由和聚合各个微服务
 * 
 * @author chennian
 * @date 2025-01-27
 */
@SpringBootApplication
@EnableFeignClients(basePackages = "com.chennian.storytelling.api.feign")
@EnableDiscoveryClient
public class StorytellingApplication {

    public static void main(String[] args) {
        SpringApplication.run(StorytellingApplication.class, args);
    }

}
