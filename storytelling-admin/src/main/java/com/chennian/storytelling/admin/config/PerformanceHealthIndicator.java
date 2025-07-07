package com.chennian.storytelling.admin.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 性能健康检查器
 */
@Component
class PerformanceHealthIndicator implements HealthIndicator {

    private final JVMPerformanceMonitor jvmMonitor;
    private final ApplicationPerformanceMonitor appMonitor;
    private final PerformanceTuningAdvisor advisor;

    public PerformanceHealthIndicator(JVMPerformanceMonitor jvmMonitor,
                                      ApplicationPerformanceMonitor appMonitor,
                                      PerformanceTuningAdvisor advisor) {
        this.jvmMonitor = jvmMonitor;
        this.appMonitor = appMonitor;
        this.advisor = advisor;
    }

    @Override
    public Health health() {
        try {
            Map<String, Object> jvmSnapshot = jvmMonitor.getPerformanceSnapshot();
            Map<String, Object> appStats = appMonitor.getPerformanceStats();

            // 检查关键性能指标
            boolean isHealthy = checkPerformanceHealth(jvmSnapshot, appStats);

            Health.Builder builder = isHealthy ? Health.up() : Health.down();

            builder.withDetail("jvm", jvmSnapshot)
                    .withDetail("application", appStats)
                    .withDetail("recommendations", advisor.analyzeAndAdvise(jvmSnapshot, appStats))
                    .withDetail("timestamp", LocalDateTime.now());

            return builder.build();

        } catch (Exception e) {
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .withDetail("timestamp", LocalDateTime.now())
                    .build();
        }
    }

    private boolean checkPerformanceHealth(Map<String, Object> jvmSnapshot, Map<String, Object> appStats) {
        // 检查内存使用率
        @SuppressWarnings("unchecked")
        Map<String, Object> memory = (Map<String, Object>) jvmSnapshot.get("memory");
        if (memory != null) {
            long heapUsed = (Long) memory.get("heapUsed");
            long heapMax = (Long) memory.get("heapMax");
            double usageRatio = (double) heapUsed / heapMax;
            if (usageRatio > 0.9) {
                return false; // 内存使用率过高
            }
        }

        // 检查线程数
        @SuppressWarnings("unchecked")
        Map<String, Object> threads = (Map<String, Object>) jvmSnapshot.get("threads");
        if (threads != null) {
            int threadCount = (Integer) threads.get("count");
            if (threadCount > 300) {
                return false; // 线程数过多
            }
        }

        return true;
    }
}
