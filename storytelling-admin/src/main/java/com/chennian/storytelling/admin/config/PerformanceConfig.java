package com.chennian.storytelling.admin.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 性能配置和调优类
 * 提供JVM调优、连接池优化、性能监控等功能
 */
@Configuration
@EnableScheduling
public class PerformanceConfig {

    @Value("${performance.thread-pool.core-size:10}")
    private int corePoolSize;

    @Value("${performance.thread-pool.max-size:50}")
    private int maxPoolSize;

    @Value("${performance.thread-pool.queue-capacity:200}")
    private int queueCapacity;

    @Value("${performance.thread-pool.keep-alive:60}")
    private int keepAliveSeconds;

    @Value("${performance.monitoring.enabled:true}")
    private boolean monitoringEnabled;

    @Value("${performance.gc.monitoring.enabled:true}")
    private boolean gcMonitoringEnabled;

    /**
     * 配置高性能线程池
     */
    @Bean(name = "performanceTaskExecutor")
    public ThreadPoolTaskExecutor performanceTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix("Performance-");
        executor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }

    /**
     * JVM性能监控器
     */
    @Bean
    public JVMPerformanceMonitor jvmPerformanceMonitor(MeterRegistry meterRegistry, JVMConfig jvmConfig) {
        return new JVMPerformanceMonitor(meterRegistry, jvmConfig);
    }

    /**
     * 应用性能监控器
     */
    @Bean
    public ApplicationPerformanceMonitor applicationPerformanceMonitor(MeterRegistry meterRegistry) {
        return new ApplicationPerformanceMonitor(meterRegistry);
    }

    /**
     * 性能调优建议器
     */
    @Bean
    public PerformanceTuningAdvisor performanceTuningAdvisor() {
        return new PerformanceTuningAdvisor();
    }
}

