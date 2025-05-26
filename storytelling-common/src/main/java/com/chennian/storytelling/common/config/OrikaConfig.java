package com.chennian.storytelling.common.config;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * dto转换实体类配置
 *
 * @author by chennian
 * @date 2023/3/14 15:58
 */
@Configuration
public class OrikaConfig {
    @Bean
    public MapperFactory mapperFactory() {
        DefaultMapperFactory.Builder builder = new DefaultMapperFactory.Builder();

        // 禁用某些可能导致问题的默认行为
        // 关闭内置 Converter
        builder.useBuiltinConverters(false);
        // 不映射 null 值
        builder.mapNulls(false);

        return builder.build();
    }

    @Bean
    public MapperFacade mapperFacade() {
        return mapperFactory().getMapperFacade();
    }
}
