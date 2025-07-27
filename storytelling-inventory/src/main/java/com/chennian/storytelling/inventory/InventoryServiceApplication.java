package com.chennian.storytelling.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 库存服务启动类
 * 
 * @author chennian
 * @since 2024-01-01
 */
@SpringBootApplication(scanBasePackages = "com.chennian.storytelling")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.chennian.storytelling")
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }
}