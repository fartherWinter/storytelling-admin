package com.chennian.storytelling.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 系统环境配置
 *
 * @author by chennian
 * @date 2023/5/17 10:11
 */
@Data
@Configuration
@ConfigurationProperties("file")
public class EnvConfig {
    /**
     * 系统环境
     */
    private String environment;
    /**
     * 数据路径地址
     */
    private String dataPath;
    /**
     * 网址信息
     */
    private String domain;
    /**
     * 路径地址
     */
    private String path;
}
