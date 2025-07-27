package com.chennian.storytelling.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 商品服务启动类
 * 
 * @author chennian
 * @since 2024-01-01
 */
@SpringBootApplication(scanBasePackages = "com.chennian.storytelling")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.chennian.storytelling")
@EnableTransactionManagement
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}