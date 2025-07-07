package com.chennian.storytelling.admin.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * JVM性能监控组件
 */
@Component
class JVMPerformanceMonitor {

    private static final Logger log = LoggerFactory.getLogger(JVMPerformanceMonitor.class);

    private final MeterRegistry meterRegistry;
    private final MemoryMXBean memoryMXBean;
    private final ThreadMXBean threadMXBean;
    private final Runtime runtime;
    private final JVMConfig jvmConfig;

    // 性能指标
    private final Timer gcTimer;
    private final Counter gcCounter;
    private final AtomicLong lastGcTime = new AtomicLong(0);
    private final AtomicLong lastGcCount = new AtomicLong(0);

    public JVMPerformanceMonitor(MeterRegistry meterRegistry, JVMConfig jvmConfig) {
        this.meterRegistry = meterRegistry;
        this.jvmConfig = jvmConfig;
        this.memoryMXBean = ManagementFactory.getMemoryMXBean();
        this.threadMXBean = ManagementFactory.getThreadMXBean();
        this.runtime = Runtime.getRuntime();

        this.gcTimer = Timer.builder("jvm.gc.time")
                .description("GC execution time")
                .register(meterRegistry);
        this.gcCounter = Counter.builder("jvm.gc.count")
                .description("GC execution count")
                .register(meterRegistry);

        if (jvmConfig.getMonitoring().isEnabled()) {
            registerGauges();
        }
    }

    @PostConstruct
    public void init() {
        log.info("JVM性能监控器已启动");
        logJVMInfo();
    }

    /**
     * 注册JVM性能指标
     */
    private void registerGauges() {
        // 内存使用率
        Gauge.builder("jvm.memory.used.ratio", this, obj -> {
                    JVMPerformanceMonitor monitor = (JVMPerformanceMonitor) obj;
                    long used = monitor.memoryMXBean.getHeapMemoryUsage().getUsed();
                    long max = monitor.memoryMXBean.getHeapMemoryUsage().getMax();
                    return max > 0 ? (double) used / max : 0;
                })
                .description("JVM memory usage ratio")
                .register(meterRegistry);

        // 线程数
        Gauge.builder("jvm.threads.count", this,
                        obj -> ((JVMPerformanceMonitor) obj).threadMXBean.getThreadCount()
                )
                .description("JVM thread count")
                .register(meterRegistry);

        // CPU使用率
        Gauge.builder("system.cpu.usage", this, monitor -> {
                    try {
                        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
                        ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
                        Object attribute = mbs.getAttribute(name, "ProcessCpuLoad");
                        return attribute instanceof Double ? (Double) attribute : 0.0;
                    } catch (Exception e) {
                        return 0.0;
                    }
                })
                .description("System CPU usage")
                .register(meterRegistry);

        // 可用处理器数
        Gauge.builder("system.cpu.count", this,
                        monitor -> runtime.availableProcessors()
                )
                .description("Available processors")
                .register(meterRegistry);
    }

    /**
     * 定期收集性能数据
     */
    @Scheduled(fixedRateString = "#{@jvmConfig.monitoring.collectionInterval}")
    public void collectPerformanceData() {
        if (!jvmConfig.getMonitoring().isEnabled()) {
            return;
        }

        try {
            if (jvmConfig.getGc().getMonitoring().isEnabled()) {
                collectGCMetrics();
            }
            if (jvmConfig.getMemory().getUsageRatio().isEnabled()) {
                checkMemoryPressure();
            }
            if (jvmConfig.getThreads().getMonitoring().isEnabled()) {
                checkThreadHealth();
            }
        } catch (Exception e) {
            log.warn("性能数据收集失败", e);
        }
    }

    /**
     * 收集GC指标
     */
    private void collectGCMetrics() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            Set<ObjectName> gcBeans = mbs.queryNames(new ObjectName("java.lang:type=GarbageCollector,*"), null);

            long totalGcTime = 0;
            long totalGcCount = 0;

            for (ObjectName gcBean : gcBeans) {
                Long collectionTime = (Long) mbs.getAttribute(gcBean, "CollectionTime");
                Long collectionCount = (Long) mbs.getAttribute(gcBean, "CollectionCount");

                if (collectionTime != null) totalGcTime += collectionTime;
                if (collectionCount != null) totalGcCount += collectionCount;
            }

            // 记录GC增量
            long gcTimeDelta = totalGcTime - lastGcTime.getAndSet(totalGcTime);
            long gcCountDelta = totalGcCount - lastGcCount.getAndSet(totalGcCount);

            if (gcTimeDelta > 0) {
                gcTimer.record(gcTimeDelta, java.util.concurrent.TimeUnit.MILLISECONDS);
            }
            if (gcCountDelta > 0) {
                gcCounter.increment(gcCountDelta);
            }

        } catch (Exception e) {
            log.warn("GC指标收集失败", e);
        }
    }

    /**
     * 检查内存压力
     */
    private void checkMemoryPressure() {
        long used = memoryMXBean.getHeapMemoryUsage().getUsed();
        long max = memoryMXBean.getHeapMemoryUsage().getMax();
        double usageRatio = (double) used / max;

        double criticalThreshold = jvmConfig.getMemory().getUsageRatio().getCriticalThreshold();
        double warningThreshold = jvmConfig.getMemory().getUsageRatio().getWarningThreshold();

        if (usageRatio > criticalThreshold) {
            log.warn("内存使用率过高: {:.2f}%, 超过严重阈值{:.1f}%, 建议进行GC调优",
                    usageRatio * 100, criticalThreshold * 100);
        } else if (usageRatio > warningThreshold) {
            log.info("内存使用率较高: {:.2f}%, 超过警告阈值{:.1f}%",
                    usageRatio * 100, warningThreshold * 100);
        }
    }

    /**
     * 检查线程健康状态
     */
    private void checkThreadHealth() {
        int threadCount = threadMXBean.getThreadCount();
        int peakThreadCount = threadMXBean.getPeakThreadCount();
        int maxThreadCount = jvmConfig.getThreads().getMonitoring().getMaxThreadCount();

        if (threadCount > maxThreadCount) {
            log.warn("线程数过多: {}, 峰值: {}, 超过配置阈值: {}",
                    threadCount, peakThreadCount, maxThreadCount);
        }

        // 检查死锁
        if (jvmConfig.getThreads().getMonitoring().isDeadlockDetection()) {
            long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();
            if (deadlockedThreads != null && deadlockedThreads.length > 0) {
                log.error("检测到死锁线程: {}", Arrays.toString(deadlockedThreads));
            }
        }
    }

    /**
     * 记录JVM基本信息
     */
    private void logJVMInfo() {
        log.info("JVM信息: 版本={}, 供应商={}, 最大内存={}MB, 可用处理器={}",
                System.getProperty("java.version"),
                System.getProperty("java.vendor"),
                runtime.maxMemory() / 1024 / 1024,
                runtime.availableProcessors());
    }

    /**
     * 获取当前JVM性能快照
     */
    public Map<String, Object> getPerformanceSnapshot() {
        Map<String, Object> snapshot = new HashMap<>();

        // 内存信息
        Map<String, Object> memory = new HashMap<>();
        memory.put("heapUsed", memoryMXBean.getHeapMemoryUsage().getUsed());
        memory.put("heapMax", memoryMXBean.getHeapMemoryUsage().getMax());
        memory.put("heapCommitted", memoryMXBean.getHeapMemoryUsage().getCommitted());
        memory.put("nonHeapUsed", memoryMXBean.getNonHeapMemoryUsage().getUsed());
        snapshot.put("memory", memory);

        // 线程信息
        Map<String, Object> threads = new HashMap<>();
        threads.put("count", threadMXBean.getThreadCount());
        threads.put("peak", threadMXBean.getPeakThreadCount());
        threads.put("daemon", threadMXBean.getDaemonThreadCount());
        snapshot.put("threads", threads);

        // 系统信息
        Map<String, Object> system = new HashMap<>();
        system.put("availableProcessors", runtime.availableProcessors());
        system.put("freeMemory", runtime.freeMemory());
        system.put("totalMemory", runtime.totalMemory());
        snapshot.put("system", system);

        snapshot.put("timestamp", LocalDateTime.now());

        return snapshot;
    }
}
