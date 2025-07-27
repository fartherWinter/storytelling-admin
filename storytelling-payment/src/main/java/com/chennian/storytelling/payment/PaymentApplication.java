package com.chennian.storytelling.payment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 支付服务启动类
 * 
 * @author chennian
 * @since 2024-01-01
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.chennian.storytelling")
@EnableCaching
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@MapperScan("com.chennian.storytelling.payment.mapper")
@ComponentScan(basePackages = "com.chennian.storytelling")
public class PaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
        System.out.println("");
        System.out.println("  ____                                  _   ");
        System.out.println(" |  _ \\ __ _ _   _ _ __ ___   ___ _ __ | |_ ");
        System.out.println(" | |_) / _` | | | | '_ ` _ \\ / _ \\ '_ \\| __|");
        System.out.println(" |  __/ (_| | |_| | | | | | |  __/ | | | |_ ");
        System.out.println(" |_|   \\__,_|\\__, |_| |_| |_|\\___|_| |_|\\__|");
        System.out.println("              |___/                          ");
        System.out.println("");
        System.out.println("故事叙述平台支付服务启动成功！");
        System.out.println("Swagger文档地址: http://localhost:8084/payment/swagger-ui.html");
        System.out.println("Druid监控地址: http://localhost:8084/payment/druid/index.html");
        System.out.println("");
    }
}