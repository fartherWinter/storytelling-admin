package com.chennian.storytelling.common.sharding;

import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * 分库分表配置
 * 支持用户表、订单表、支付表等核心业务表的分片
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Configuration
public class ShardingConfig {

    /**
     * 分片数据源配置
     */
    @Bean
    @Primary
    public DataSource shardingDataSource() throws SQLException {
        // 配置真实数据源
        Map<String, DataSource> dataSourceMap = createDataSourceMap();
        
        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        
        // 配置用户表分片规则
        shardingRuleConfig.getTables().add(getUserTableRuleConfiguration());
        
        // 配置订单表分片规则
        shardingRuleConfig.getTables().add(getOrderTableRuleConfiguration());
        
        // 配置支付表分片规则
        shardingRuleConfig.getTables().add(getPaymentTableRuleConfiguration());
        
        // 配置分片算法
        shardingRuleConfig.getShardingAlgorithms().put("database_inline", 
            new ShardingSphereAlgorithmConfiguration("INLINE", createDatabaseShardingProps()));
        shardingRuleConfig.getShardingAlgorithms().put("table_inline", 
            new ShardingSphereAlgorithmConfiguration("INLINE", createTableShardingProps()));
        shardingRuleConfig.getShardingAlgorithms().put("user_database_inline", 
            new ShardingSphereAlgorithmConfiguration("INLINE", createUserDatabaseShardingProps()));
        shardingRuleConfig.getShardingAlgorithms().put("user_table_inline", 
            new ShardingSphereAlgorithmConfiguration("INLINE", createUserTableShardingProps()));
        
        // 配置默认分库策略
        shardingRuleConfig.setDefaultDatabaseShardingStrategy(
            new StandardShardingStrategyConfiguration("user_id", "database_inline"));
        
        // 配置默认分表策略
        shardingRuleConfig.setDefaultTableShardingStrategy(
            new StandardShardingStrategyConfiguration("id", "table_inline"));
        
        Properties props = new Properties();
        props.setProperty("sql-show", "true");
        
        return ShardingSphereDataSourceFactory.createDataSource(dataSourceMap, 
            Collections.singleton(shardingRuleConfig), props);
    }

    /**
     * 创建数据源映射
     */
    private Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        
        // 这里应该配置实际的数据源
        // 示例：配置4个数据库实例
        for (int i = 0; i < 4; i++) {
            dataSourceMap.put("ds" + i, createDataSource("ds" + i));
        }
        
        return dataSourceMap;
    }

    /**
     * 创建单个数据源
     */
    private DataSource createDataSource(String dataSourceName) {
        // 这里应该返回实际的数据源配置
        // 例如：HikariDataSource, DruidDataSource等
        // 为了示例，这里返回null，实际使用时需要配置具体的数据源
        return null;
    }

    /**
     * 用户表分片规则配置
     */
    private ShardingTableRuleConfiguration getUserTableRuleConfiguration() {
        ShardingTableRuleConfiguration config = new ShardingTableRuleConfiguration("t_user", "ds${0..3}.t_user_${0..15}");
        config.setDatabaseShardingStrategy(new StandardShardingStrategyConfiguration("id", "user_database_inline"));
        config.setTableShardingStrategy(new StandardShardingStrategyConfiguration("id", "user_table_inline"));
        return config;
    }

    /**
     * 订单表分片规则配置
     */
    private ShardingTableRuleConfiguration getOrderTableRuleConfiguration() {
        ShardingTableRuleConfiguration config = new ShardingTableRuleConfiguration("t_order", "ds${0..3}.t_order_${0..15}");
        config.setDatabaseShardingStrategy(new StandardShardingStrategyConfiguration("user_id", "database_inline"));
        config.setTableShardingStrategy(new StandardShardingStrategyConfiguration("id", "table_inline"));
        return config;
    }

    /**
     * 支付表分片规则配置
     */
    private ShardingTableRuleConfiguration getPaymentTableRuleConfiguration() {
        ShardingTableRuleConfiguration config = new ShardingTableRuleConfiguration("t_payment_order", "ds${0..3}.t_payment_order_${0..15}");
        config.setDatabaseShardingStrategy(new StandardShardingStrategyConfiguration("user_id", "database_inline"));
        config.setTableShardingStrategy(new StandardShardingStrategyConfiguration("id", "table_inline"));
        return config;
    }

    /**
     * 数据库分片算法配置
     */
    private Properties createDatabaseShardingProps() {
        Properties props = new Properties();
        props.setProperty("algorithm-expression", "ds${user_id % 4}");
        return props;
    }

    /**
     * 表分片算法配置
     */
    private Properties createTableShardingProps() {
        Properties props = new Properties();
        props.setProperty("algorithm-expression", "t_order_${id % 16}");
        return props;
    }

    /**
     * 用户表数据库分片算法配置
     */
    private Properties createUserDatabaseShardingProps() {
        Properties props = new Properties();
        props.setProperty("algorithm-expression", "ds${id % 4}");
        return props;
    }

    /**
     * 用户表分片算法配置
     */
    private Properties createUserTableShardingProps() {
        Properties props = new Properties();
        props.setProperty("algorithm-expression", "t_user_${id % 16}");
        return props;
    }
}