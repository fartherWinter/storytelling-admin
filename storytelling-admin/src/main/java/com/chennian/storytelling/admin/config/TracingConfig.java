package com.chennian.storytelling.admin.config;

import brave.sampler.Sampler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.okhttp3.OkHttpSender;
import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;

import javax.annotation.PreDestroy;

/**
 * 链路追踪配置类
 * 集成Spring Cloud Sleuth和Zipkin，提供分布式链路追踪能力
 */
@Configuration
@ConditionalOnProperty(name = "tracing.enabled", havingValue = "true", matchIfMissing = true)
public class TracingConfig {

    @Value("${tracing.zipkin.base-url:http://localhost:9411}")
    private String zipkinBaseUrl;

    @Value("${tracing.sample-rate:0.1}")
    private float sampleRate;

    @Value("${tracing.service-name:storytelling-conference}")
    private String serviceName;

    /**
     * 配置采样率
     * 采样率决定了多少比例的请求会被追踪
     */
    @Bean
    public Sampler alwaysSampler() {
        return Sampler.create(sampleRate);
    }

    /**
     * 配置Zipkin发送器
     * 用于将追踪数据发送到Zipkin服务器
     */
    @Bean
    @ConditionalOnProperty(name = "tracing.zipkin.enabled", havingValue = "true", matchIfMissing = true)
    public OkHttpSender sender() {
        return OkHttpSender.create(zipkinBaseUrl + "/api/v2/spans");
    }

    /**
     * 配置异步报告器
     * 异步发送追踪数据，避免阻塞主线程
     */
    @Bean
    @ConditionalOnProperty(name = "tracing.zipkin.enabled", havingValue = "true", matchIfMissing = true)
    public AsyncReporter<zipkin2.Span> spanReporter() {
        return AsyncReporter.create(sender());
    }

    /**
     * 配置RestTemplate以支持链路追踪
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 清理资源
     */
    @PreDestroy
    public void cleanup() {
        try {
            if (spanReporter() != null) {
                spanReporter().close();
            }
        } catch (Exception e) {
            // 忽略清理异常
        }
    }
}

