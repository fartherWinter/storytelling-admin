package com.chennian.storytelling.admin.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * 应用性能监控组件
 */
@Component
class ApplicationPerformanceMonitor {

    private static final Logger log = LoggerFactory.getLogger(ApplicationPerformanceMonitor.class);

    private final MeterRegistry meterRegistry;
    private final Map<String, LongAdder> operationCounters = new ConcurrentHashMap<>();
    private final Map<String, Timer> operationTimers = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> errorCounters = new ConcurrentHashMap<>();

    public ApplicationPerformanceMonitor(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    /**
     * 记录操作执行时间
     */
    public void recordOperationTime(String operation, long timeMs) {
        Timer timer = operationTimers.computeIfAbsent(operation,
                op -> Timer.builder("app.operation.time")
                        .tag("operation", op)
                        .description("Operation execution time")
                        .register(meterRegistry));
        timer.record(timeMs, java.util.concurrent.TimeUnit.MILLISECONDS);

        // 同时记录操作次数
        operationCounters.computeIfAbsent(operation, op -> new LongAdder()).increment();
    }

    /**
     * 记录错误
     */
    public void recordError(String operation, String errorType) {
        String key = operation + "." + errorType;
        errorCounters.computeIfAbsent(key, k -> new AtomicLong(0)).incrementAndGet();

        Counter.builder("app.operation.errors")
                .tag("operation", operation)
                .tag("error.type", errorType)
                .description("Operation error count")
                .register(meterRegistry)
                .increment();
    }

    /**
     * 获取性能统计
     */
    public Map<String, Object> getPerformanceStats() {
        Map<String, Object> stats = new HashMap<>();

        // 操作统计
        Map<String, Object> operations = new HashMap<>();
        for (Map.Entry<String, LongAdder> entry : operationCounters.entrySet()) {
            String operation = entry.getKey();
            long count = entry.getValue().sum();

            Timer timer = operationTimers.get(operation);
            double avgTime = timer != null ? timer.mean(java.util.concurrent.TimeUnit.MILLISECONDS) : 0;

            Map<String, Object> opStats = new HashMap<>();
            opStats.put("count", count);
            opStats.put("avgTimeMs", avgTime);
            opStats.put("totalTimeMs", timer != null ? timer.totalTime(java.util.concurrent.TimeUnit.MILLISECONDS) : 0);

            operations.put(operation, opStats);
        }
        stats.put("operations", operations);

        // 错误统计
        Map<String, Long> errors = new HashMap<>();
        for (Map.Entry<String, AtomicLong> entry : errorCounters.entrySet()) {
            errors.put(entry.getKey(), entry.getValue().get());
        }
        stats.put("errors", errors);

        stats.put("timestamp", LocalDateTime.now());

        return stats;
    }

    /**
     * 重置统计数据
     */
    public void resetStats() {
        operationCounters.clear();
        operationTimers.clear();
        errorCounters.clear();
        log.info("应用性能统计数据已重置");
    }
}
