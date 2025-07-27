package com.chennian.storytelling.admin.config;

import brave.Tracing;
import brave.http.HttpTracing;
import brave.sampler.Sampler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Sender;
import zipkin2.reporter.okhttp3.OkHttpSender;

/**
 * Admin 服务链路追踪配置
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Configuration
@Slf4j
public class TracingConfiguration {
    
    @Value("${spring.zipkin.base-url:http://localhost:9411}")
    private String zipkinBaseUrl;
    
    @Value("${spring.sleuth.sampler.probability:1.0}")
    private float sampleRate;
    
    @Value("${spring.application.name:storytelling-admin}")
    private String serviceName;
    
    /**
     * 配置采样器
     */
    @Bean
    public Sampler alwaysSampler() {
        return Sampler.create(sampleRate);
    }
    
    /**
     * 配置 Zipkin Sender
     */
    @Bean
    public Sender sender() {
        return OkHttpSender.create(zipkinBaseUrl + "/api/v2/spans");
    }
    
    /**
     * 配置 AsyncReporter
     */
    @Bean
    public AsyncReporter<Span> spanReporter() {
        return AsyncReporter.create(sender());
    }
    
    /**
     * 配置 Tracing
     */
    @Bean
    public Tracing tracing() {
        return Tracing.newBuilder()
                .localServiceName(serviceName)
                .spanReporter(spanReporter())
                .sampler(alwaysSampler())
                .build();
    }
    
    /**
     * 配置 HTTP 追踪
     */
    @Bean
    public HttpTracing httpTracing() {
        return HttpTracing.create(tracing());
    }
    
    /**
     * 配置 RestTemplate 用于链路追踪
     */
    @Bean
    public RestTemplate tracingRestTemplate() {
        return new RestTemplate();
    }
}