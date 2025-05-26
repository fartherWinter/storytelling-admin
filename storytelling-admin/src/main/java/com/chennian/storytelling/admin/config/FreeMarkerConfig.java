//package com.chennian.storytelling.admin.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
//
//import java.util.Properties;
//
///**
// * FreeMarker配置类
// *
// * @author chennian
// */
//@Configuration
//public class FreeMarkerConfig {
//
//    /**
//     * 配置FreeMarker模板引擎
//     */
//    @Bean
//    public FreeMarkerConfigurationFactoryBean freeMarkerConfigurationFactoryBean() {
//        FreeMarkerConfigurationFactoryBean factoryBean = new FreeMarkerConfigurationFactoryBean();
//        factoryBean.setTemplateLoaderPath("classpath:/templates/");
//        factoryBean.setDefaultEncoding("UTF-8");
//
//        // 设置FreeMarker属性
//        Properties properties = new Properties();
//        properties.setProperty("template_update_delay", "0");
//        properties.setProperty("locale", "zh_CN");
//        properties.setProperty("datetime_format", "yyyy-MM-dd HH:mm:ss");
//        properties.setProperty("date_format", "yyyy-MM-dd");
//        properties.setProperty("number_format", "#.##");
//
//        factoryBean.setFreemarkerSettings(properties);
//        return factoryBean;
//    }
//}