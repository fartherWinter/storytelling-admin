package com.chennian.storytelling.common.performance;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 性能测试控制器
 * 提供性能监控、测试和优化建议的REST API
 * 
 * @author chennian
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/performance")
@Tag(name = "性能测试", description = "性能监控、测试和优化相关接口")
public class PerformanceController {

    @Autowired
    private PerformanceMonitor performanceMonitor;

    @Autowired
    private PerformanceTester performanceTester;

    @Autowired
    private PerformanceOptimizer performanceOptimizer;

    /**
     * 获取性能报告
     */
    @GetMapping("/report")
    @Operation(summary = "获取性能报告", description = "获取当前系统的性能监控报告")
    public ResponseEntity<PerformanceMonitor.PerformanceReport> getPerformanceReport() {
        PerformanceMonitor.PerformanceReport report = performanceMonitor.getPerformanceReport();
        return ResponseEntity.ok(report);
    }

    /**
     * 开始性能监控
     */
    @PostMapping("/monitoring/start")
    @Operation(summary = "开始性能监控", description = "启动系统性能监控")
    public ResponseEntity<String> startMonitoring() {
        performanceMonitor.startMonitoring();
        return ResponseEntity.ok("Performance monitoring started");
    }

    /**
     * 停止性能监控
     */
    @PostMapping("/monitoring/stop")
    @Operation(summary = "停止性能监控", description = "停止系统性能监控")
    public ResponseEntity<String> stopMonitoring() {
        performanceMonitor.stopMonitoring();
        return ResponseEntity.ok("Performance monitoring stopped");
    }

    /**
     * 执行压力测试
     */
    @PostMapping("/stress-test")
    @Operation(summary = "执行压力测试", description = "对指定接口执行压力测试")
    public ResponseEntity<CompletableFuture<PerformanceTester.StressTestResult>> executeStressTest(
            @Parameter(description = "测试名称") @RequestParam String testName,
            @Parameter(description = "目标URL") @RequestParam String targetUrl,
            @Parameter(description = "并发用户数") @RequestParam(defaultValue = "10") int concurrentUsers,
            @Parameter(description = "测试持续时间（秒）") @RequestParam(defaultValue = "60") int durationSeconds,
            @Parameter(description = "每秒请求数") @RequestParam(defaultValue = "0") int requestsPerSecond) {
        
        PerformanceTester.StressTestConfig config = new PerformanceTester.StressTestConfig(
                concurrentUsers, durationSeconds, requestsPerSecond);
        
        // 创建HTTP请求任务
        Runnable httpTask = () -> {
            try {
                // 这里可以集成HTTP客户端执行实际的HTTP请求
                // 为了演示，这里使用简单的延迟模拟
                Thread.sleep((long) (Math.random() * 100 + 50)); // 50-150ms随机延迟
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        
        CompletableFuture<PerformanceTester.StressTestResult> future = 
                CompletableFuture.supplyAsync(() -> 
                        performanceTester.executeStressTest(testName, httpTask, config));
        
        return ResponseEntity.ok(future);
    }

    /**
     * 执行基准测试
     */
    @PostMapping("/benchmark")
    @Operation(summary = "执行基准测试", description = "对指定方法执行基准测试")
    public ResponseEntity<PerformanceTester.BenchmarkResult> executeBenchmark(
            @Parameter(description = "测试名称") @RequestParam String testName,
            @Parameter(description = "迭代次数") @RequestParam(defaultValue = "1000") int iterations,
            @Parameter(description = "测试类型") @RequestParam(defaultValue = "cpu") String testType) {
        
        // 根据测试类型创建不同的任务
        Runnable task = createBenchmarkTask(testType);
        
        PerformanceTester.BenchmarkResult result = 
                performanceTester.executeBenchmark(testName, task, iterations);
        
        return ResponseEntity.ok(result);
    }

    /**
     * 执行并发测试
     */
    @PostMapping("/concurrency-test")
    @Operation(summary = "执行并发测试", description = "测试系统的并发处理能力")
    public ResponseEntity<PerformanceTester.ConcurrencyTestResult<String>> executeConcurrencyTest(
            @Parameter(description = "测试名称") @RequestParam String testName,
            @Parameter(description = "并发数") @RequestParam(defaultValue = "10") int concurrency,
            @Parameter(description = "每个线程的任务数") @RequestParam(defaultValue = "100") int tasksPerThread) {
        
        // 创建并发任务
        java.util.function.Supplier<String> taskSupplier = () -> {
            try {
                // 模拟业务处理
                Thread.sleep((long) (Math.random() * 10 + 5)); // 5-15ms随机延迟
                return "Task completed at " + System.currentTimeMillis();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Task interrupted";
            }
        };
        
        PerformanceTester.ConcurrencyTestResult<String> result = 
                performanceTester.executeConcurrencyTest(testName, taskSupplier, concurrency, tasksPerThread);
        
        return ResponseEntity.ok(result);
    }

    /**
     * 获取优化建议
     */
    @GetMapping("/optimization/suggestions")
    @Operation(summary = "获取优化建议", description = "基于当前性能数据生成优化建议")
    public ResponseEntity<List<PerformanceOptimizer.OptimizationSuggestion>> getOptimizationSuggestions() {
        List<PerformanceOptimizer.OptimizationSuggestion> suggestions = 
                performanceOptimizer.analyzeAndSuggest();
        return ResponseEntity.ok(suggestions);
    }

    /**
     * 获取优化报告
     */
    @GetMapping("/optimization/report")
    @Operation(summary = "获取优化报告", description = "获取完整的性能优化报告")
    public ResponseEntity<PerformanceOptimizer.OptimizationReport> getOptimizationReport() {
        PerformanceOptimizer.OptimizationReport report = 
                performanceOptimizer.generateOptimizationReport();
        return ResponseEntity.ok(report);
    }

    /**
     * 记录方法执行时间
     */
    @PostMapping("/record/method")
    @Operation(summary = "记录方法执行时间", description = "手动记录方法的执行时间")
    public ResponseEntity<String> recordMethodExecution(
            @Parameter(description = "方法名称") @RequestParam String methodName,
            @Parameter(description = "执行时间（毫秒）") @RequestParam long executionTime) {
        
        performanceMonitor.recordMethodExecution(methodName, executionTime);
        return ResponseEntity.ok("Method execution recorded");
    }

    /**
     * 记录API调用
     */
    @PostMapping("/record/api")
    @Operation(summary = "记录API调用", description = "手动记录API调用的性能数据")
    public ResponseEntity<String> recordApiCall(
            @Parameter(description = "API路径") @RequestParam String apiPath,
            @Parameter(description = "响应时间（毫秒）") @RequestParam long responseTime,
            @Parameter(description = "是否成功") @RequestParam boolean success) {
        
        performanceMonitor.recordApiCall(apiPath, responseTime, success);
        return ResponseEntity.ok("API call recorded");
    }

    /**
     * 记录错误
     */
    @PostMapping("/record/error")
    @Operation(summary = "记录错误", description = "手动记录系统错误")
    public ResponseEntity<String> recordError(
            @Parameter(description = "错误源") @RequestParam String source,
            @Parameter(description = "错误类型") @RequestParam String errorType) {
        
        performanceMonitor.recordError(source, errorType);
        return ResponseEntity.ok("Error recorded");
    }

    /**
     * 获取系统健康状态
     */
    @GetMapping("/health")
    @Operation(summary = "获取系统健康状态", description = "获取系统的整体健康评分")
    public ResponseEntity<HealthStatus> getHealthStatus() {
        PerformanceOptimizer.OptimizationReport report = 
                performanceOptimizer.generateOptimizationReport();
        
        HealthStatus status = new HealthStatus(
                report.getHealthScore(),
                getHealthLevel(report.getHealthScore()),
                report.getSuggestions().size(),
                report.getHighPrioritySuggestions(),
                System.currentTimeMillis()
        );
        
        return ResponseEntity.ok(status);
    }

    /**
     * 创建基准测试任务
     */
    private Runnable createBenchmarkTask(String testType) {
        switch (testType.toLowerCase()) {
            case "cpu":
                return () -> {
                    // CPU密集型任务
                    double result = 0;
                    for (int i = 0; i < 10000; i++) {
                        result += Math.sqrt(i) * Math.sin(i);
                    }
                };
            case "memory":
                return () -> {
                    // 内存分配任务
                    java.util.List<String> list = new java.util.ArrayList<>();
                    for (int i = 0; i < 1000; i++) {
                        list.add("String " + i);
                    }
                };
            case "io":
                return () -> {
                    // I/O模拟任务
                    try {
                        Thread.sleep(1); // 模拟I/O等待
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                };
            default:
                return () -> {
                    // 默认任务
                    try {
                        Thread.sleep((long) (Math.random() * 5 + 1));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                };
        }
    }

    /**
     * 获取健康等级
     */
    private String getHealthLevel(int healthScore) {
        if (healthScore >= 90) {
            return "优秀";
        } else if (healthScore >= 80) {
            return "良好";
        } else if (healthScore >= 70) {
            return "一般";
        } else if (healthScore >= 60) {
            return "较差";
        } else {
            return "危险";
        }
    }

    /**
     * 健康状态
     */
    public static class HealthStatus {
        private final int healthScore;
        private final String healthLevel;
        private final int totalSuggestions;
        private final long highPrioritySuggestions;
        private final long timestamp;

        public HealthStatus(int healthScore, String healthLevel, int totalSuggestions,
                          long highPrioritySuggestions, long timestamp) {
            this.healthScore = healthScore;
            this.healthLevel = healthLevel;
            this.totalSuggestions = totalSuggestions;
            this.highPrioritySuggestions = highPrioritySuggestions;
            this.timestamp = timestamp;
        }

        // Getters
        public int getHealthScore() { return healthScore; }
        public String getHealthLevel() { return healthLevel; }
        public int getTotalSuggestions() { return totalSuggestions; }
        public long getHighPrioritySuggestions() { return highPrioritySuggestions; }
        public long getTimestamp() { return timestamp; }

        @Override
        public String toString() {
            return String.format("HealthStatus{score=%d, level='%s', suggestions=%d, highPriority=%d}",
                               healthScore, healthLevel, totalSuggestions, highPrioritySuggestions);
        }
    }
}