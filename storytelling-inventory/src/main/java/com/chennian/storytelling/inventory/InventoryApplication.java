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
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
public class InventoryApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
        System.out.println("");
        System.out.println("   _____ _                   _       _ _ _             ");
        System.out.println("  / ____| |                 | |     | | (_)            ");
        System.out.println(" | (___ | |_ ___  _ __ _   _ | |_ ___| | |_ _ __   __ _  ");
        System.out.println("  \___ \\| __/ _ \\| '__| | | || __/ _ \\ | | | '_ \\ / _` | ");
        System.out.println("  ____) | || (_) | |  | |_| || ||  __/ | | | | | | (_| | ");
        System.out.println(" |_____/ \\__\\___/|_|   \\__, | \\__\___|_|_|_|_| |_|\\__, | ");
        System.out.println("                         __/ |                    __/ | ");
        System.out.println("                        |___/                    |___/  ");
        System.out.println("");
        System.out.println("库存服务启动成功！");
        System.out.println("API文档地址: http://localhost:8083/inventory/swagger-ui.html");
        System.out.println("健康检查地址: http://localhost:8083/inventory/actuator/health");
        System.out.println("");
    }
}