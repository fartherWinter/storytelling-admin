package com.chennian.storytelling.inventory.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 库存模块配置类
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.chennian.storytelling.inventory")
@MapperScan(basePackages = "com.chennian.storytelling.inventory.mapper")
public class InventoryConfig {
    
    // 库存模块相关配置
    
}