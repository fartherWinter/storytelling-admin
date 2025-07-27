package com.chennian.storytelling.common.performance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * 性能监控器
 * 用于监控系统性能指标，支持压力测试和性能调优
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Component
public class PerformanceMonitor {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceMonitor.class);

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private final ConcurrentMap<String, MethodMetrics> methodMetrics = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, ApiMetrics> apiMetrics = new ConcurrentHashMap<>();
    
    private final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    private final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    
    private volatile boolean monitoring = false;
    private final AtomicLong startTime = new AtomicLong();

    @PostConstruct
    public void init() {
        startMonitoring();
        logger.info("Performance monitor initialized");
    }

    @PreDestroy
    public void destroy() {
        stopMonitoring();
        scheduler.shutdown();
        logger.info("Performance monitor destroyed");
    }

    /**
     * 开始监控
     */
    public void startMonitoring() {
        if (!monitoring) {
            monitoring = true;
            startTime.set(System.currentTimeMillis());
            
            // 每30秒输出一次性能报告
            scheduler.scheduleAtFixedRate(this::logPerformanceReport, 30, 30, TimeUnit.SECONDS);
            
            // 每5分钟清理过期的指标数据
            scheduler.scheduleAtFixedRate(this::cleanupExpiredMetrics, 5, 5, TimeUnit.MINUTES);
            
            logger.info("Performance monitoring started");
        }
    }

    /**
     * 停止监控
     */
    public void stopMonitoring() {
        monitoring = false;
        logger.info("Performance monitoring stopped");
    }

    /**
     * 记录方法执行时间
     * 
     * @param methodName 方法名
     * @param executionTime 执行时间（毫秒）
     */
    public void recordMethodExecution(String methodName, long executionTime) {
        if (!monitoring) return;
        
        methodMetrics.computeIfAbsent(methodName, k -> new MethodMetrics())
                    .recordExecution(executionTime);
    }

    /**
     * 记录API调用
     * 
     * @param apiPath API路径
     * @param responseTime 响应时间（毫秒）
     * @param success 是否成功
     */
    public void recordApiCall(String apiPath, long responseTime, boolean success) {
        if (!monitoring) return;
        
        apiMetrics.computeIfAbsent(apiPath, k -> new ApiMetrics())
                 .recordCall(responseTime, success);
    }

    /**
     * 记录错误
     * 
     * @param source 错误源
     * @param errorType 错误类型
     */
    public void recordError(String source, String errorType) {
        if (!monitoring) return;
        
        String key = source + ":" + errorType;
        methodMetrics.computeIfAbsent(key, k -> new MethodMetrics())
                    .recordError();
    }

    /**
     * 获取系统性能报告
     * 
     * @return 性能报告
     */
    public PerformanceReport getPerformanceReport() {
        return new PerformanceReport(
                getSystemMetrics(),
                getMethodMetricsReport(),
                getApiMetricsReport(),
                System.currentTimeMillis() - startTime.get()
        );
    }

    /**
     * 获取系统指标
     */
    private SystemMetrics getSystemMetrics() {
        long heapUsed = memoryMXBean.getHeapMemoryUsage().getUsed();
        long heapMax = memoryMXBean.getHeapMemoryUsage().getMax();
        long nonHeapUsed = memoryMXBean.getNonHeapMemoryUsage().getUsed();
        int threadCount = threadMXBean.getThreadCount();
        int peakThreadCount = threadMXBean.getPeakThreadCount();
        
        return new SystemMetrics(
                heapUsed, heapMax, nonHeapUsed,
                threadCount, peakThreadCount,
                Runtime.getRuntime().availableProcessors()
        );
    }

    /**
     * 获取方法指标报告
     */
    private java.util.Map<String, MethodMetrics> getMethodMetricsReport() {
        return new ConcurrentHashMap<>(methodMetrics);
    }

    /**
     * 获取API指标报告
     */
    private java.util.Map<String, ApiMetrics> getApiMetricsReport() {
        return new ConcurrentHashMap<>(apiMetrics);
    }

    /**
     * 输出性能报告日志
     */
    private void logPerformanceReport() {
        try {
            PerformanceReport report = getPerformanceReport();
            logger.info("Performance Report: {}", report);
        } catch (Exception e) {
            logger.error("Error generating performance report", e);
        }
    }

    /**
     * 清理过期的指标数据
     */
    private void cleanupExpiredMetrics() {
        long currentTime = System.currentTimeMillis();
        long expireTime = currentTime - TimeUnit.HOURS.toMillis(1); // 1小时过期
        
        methodMetrics.entrySet().removeIf(entry -> 
            entry.getValue().getLastUpdateTime() < expireTime);
        
        apiMetrics.entrySet().removeIf(entry -> 
            entry.getValue().getLastUpdateTime() < expireTime);
    }

    /**
     * 方法执行指标
     */
    public static class MethodMetrics {
        private final LongAdder totalCalls = new LongAdder();
        private final LongAdder totalTime = new LongAdder();
        private final LongAdder errorCount = new LongAdder();
        private volatile long minTime = Long.MAX_VALUE;
        private volatile long maxTime = 0;
        private volatile long lastUpdateTime = System.currentTimeMillis();

        public void recordExecution(long executionTime) {
            totalCalls.increment();
            totalTime.add(executionTime);
            
            if (executionTime < minTime) {
                minTime = executionTime;
            }
            if (executionTime > maxTime) {
                maxTime = executionTime;
            }
            
            lastUpdateTime = System.currentTimeMillis();
        }

        public void recordError() {
            errorCount.increment();
            lastUpdateTime = System.currentTimeMillis();
        }

        public long getTotalCalls() { return totalCalls.sum(); }
        public long getTotalTime() { return totalTime.sum(); }
        public long getErrorCount() { return errorCount.sum(); }
        public long getMinTime() { return minTime == Long.MAX_VALUE ? 0 : minTime; }
        public long getMaxTime() { return maxTime; }
        public double getAverageTime() {
            long calls = getTotalCalls();
            return calls > 0 ? (double) getTotalTime() / calls : 0;
        }
        public double getErrorRate() {
            long calls = getTotalCalls();
            return calls > 0 ? (double) getErrorCount() / calls : 0;
        }
        public long getLastUpdateTime() { return lastUpdateTime; }

        @Override
        public String toString() {
            return String.format("MethodMetrics{calls=%d, avgTime=%.2fms, minTime=%dms, maxTime=%dms, errors=%d, errorRate=%.2f%%}",
                    getTotalCalls(), getAverageTime(), getMinTime(), getMaxTime(), 
                    getErrorCount(), getErrorRate() * 100);
        }
    }

    /**
     * API调用指标
     */
    public static class ApiMetrics {
        private final LongAdder totalCalls = new LongAdder();
        private final LongAdder successCalls = new LongAdder();
        private final LongAdder totalResponseTime = new LongAdder();
        private volatile long minResponseTime = Long.MAX_VALUE;
        private volatile long maxResponseTime = 0;
        private volatile long lastUpdateTime = System.currentTimeMillis();

        public void recordCall(long responseTime, boolean success) {
            totalCalls.increment();
            if (success) {
                successCalls.increment();
            }
            
            totalResponseTime.add(responseTime);
            
            if (responseTime < minResponseTime) {
                minResponseTime = responseTime;
            }
            if (responseTime > maxResponseTime) {
                maxResponseTime = responseTime;
            }
            
            lastUpdateTime = System.currentTimeMillis();
        }

        public long getTotalCalls() { return totalCalls.sum(); }
        public long getSuccessCalls() { return successCalls.sum(); }
        public long getFailedCalls() { return getTotalCalls() - getSuccessCalls(); }
        public long getTotalResponseTime() { return totalResponseTime.sum(); }
        public long getMinResponseTime() { return minResponseTime == Long.MAX_VALUE ? 0 : minResponseTime; }
        public long getMaxResponseTime() { return maxResponseTime; }
        public double getAverageResponseTime() {
            long calls = getTotalCalls();
            return calls > 0 ? (double) getTotalResponseTime() / calls : 0;
        }
        public double getSuccessRate() {
            long calls = getTotalCalls();
            return calls > 0 ? (double) getSuccessCalls() / calls : 0;
        }
        public long getLastUpdateTime() { return lastUpdateTime; }

        @Override
        public String toString() {
            return String.format("ApiMetrics{calls=%d, successRate=%.2f%%, avgResponseTime=%.2fms, minTime=%dms, maxTime=%dms}",
                    getTotalCalls(), getSuccessRate() * 100, getAverageResponseTime(), 
                    getMinResponseTime(), getMaxResponseTime());
        }
    }

    /**
     * 系统指标
     */
    public static class SystemMetrics {
        private final long heapUsed;
        private final long heapMax;
        private final long nonHeapUsed;
        private final int threadCount;
        private final int peakThreadCount;
        private final int availableProcessors;

        public SystemMetrics(long heapUsed, long heapMax, long nonHeapUsed,
                           int threadCount, int peakThreadCount, int availableProcessors) {
            this.heapUsed = heapUsed;
            this.heapMax = heapMax;
            this.nonHeapUsed = nonHeapUsed;
            this.threadCount = threadCount;
            this.peakThreadCount = peakThreadCount;
            this.availableProcessors = availableProcessors;
        }

        public long getHeapUsed() { return heapUsed; }
        public long getHeapMax() { return heapMax; }
        public long getNonHeapUsed() { return nonHeapUsed; }
        public int getThreadCount() { return threadCount; }
        public int getPeakThreadCount() { return peakThreadCount; }
        public int getAvailableProcessors() { return availableProcessors; }
        public double getHeapUsagePercentage() {
            return heapMax > 0 ? (double) heapUsed / heapMax * 100 : 0;
        }

        @Override
        public String toString() {
            return String.format("SystemMetrics{heapUsed=%dMB, heapMax=%dMB, heapUsage=%.2f%%, nonHeapUsed=%dMB, threads=%d, peakThreads=%d, processors=%d}",
                    heapUsed / 1024 / 1024, heapMax / 1024 / 1024, getHeapUsagePercentage(),
                    nonHeapUsed / 1024 / 1024, threadCount, peakThreadCount, availableProcessors);
        }
    }

    /**
     * 性能报告
     */
    public static class PerformanceReport {
        private final SystemMetrics systemMetrics;
        private final java.util.Map<String, MethodMetrics> methodMetrics;
        private final java.util.Map<String, ApiMetrics> apiMetrics;
        private final long uptime;

        public PerformanceReport(SystemMetrics systemMetrics,
                               java.util.Map<String, MethodMetrics> methodMetrics,
                               java.util.Map<String, ApiMetrics> apiMetrics,
                               long uptime) {
            this.systemMetrics = systemMetrics;
            this.methodMetrics = methodMetrics;
            this.apiMetrics = apiMetrics;
            this.uptime = uptime;
        }

        public SystemMetrics getSystemMetrics() { return systemMetrics; }
        public java.util.Map<String, MethodMetrics> getMethodMetrics() { return methodMetrics; }
        public java.util.Map<String, ApiMetrics> getApiMetrics() { return apiMetrics; }
        public long getUptime() { return uptime; }

        @Override
        public String toString() {
            return String.format("PerformanceReport{uptime=%dms, system=%s, methods=%d, apis=%d}",
                    uptime, systemMetrics, methodMetrics.size(), apiMetrics.size());
        }
    }
}