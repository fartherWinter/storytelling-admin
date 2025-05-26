package com.chennian.storytelling.admin.config;

/**
 * @author by chennian
 * @date 2025/5/20.
 */

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                // 接口文档标题
                .info(new Info().title("API接口文档")
                        // 接口文档简介
                        .description("pai api doc")
                        // 接口文档版本
                        .version("0.0.1-SNAPSHOT")
                        // 开发者联系方式
                        .contact(new Contact().name("chennian")
                                .email("cnianxdjs@163.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("paicoding api doc")
                        .url("http://127.0.0.1:8888"));
    }
}

