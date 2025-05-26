//package com.chennian.storytelling.admin.config;
//
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//
//import org.springdoc.core.models.GroupedOpenApi;
//import org.springframework.context.annotation.Bean;
//
///**
// * @author by chennian
// * @date 2025/4/28.
// */
//public class SwaggerConfiguration {
//    @Bean
//    public GroupedOpenApi createRestApi() {
//        return GroupedOpenApi.builder()
//                .group("接口文档")
//                .packagesToScan("com.chennian.api").build();
//    }
//
//
//    @Bean
//    public OpenAPI springShopOpenApi() {
//        return new OpenAPI()
//                .info(new Info().title("接口文档")
//                        .description("接口文档，openapi3.0 接口，用于前端对接")
//                        .version("v0.0.1"));
//    }
//}
