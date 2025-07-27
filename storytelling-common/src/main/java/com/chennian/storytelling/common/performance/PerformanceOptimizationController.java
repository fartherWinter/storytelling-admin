package com.chennian.storytelling.common.performance;

import com.chennian.storytelling.common.cache.CachePerformanceOptimizer;
import com.chennian.storytelling.common.database.DatabasePerformanceOptimizer;
import com.chennian.storytelling.common.mq.MessageQueuePerformanceOptimizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 性能优化统一控制器
 * 整合缓存、数据库、消息队列等各个模块的性能优化功能
 * 
 * @author chennian
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/performance/optimization")
public class PerformanceOptimizationController {

    @Autowired
    private PerformanceMonitor performanceMonitor;
    
    @Autowired
    private PerformanceTester performanceTester;
    
    @Autowired
    private PerformanceOptimizer performanceOptimizer;
    
    @Autowired
    private CachePerformanceOptimizer cacheOptimizer;
    
    @Autowired
    private DatabasePerformanceOptimizer databaseOptimizer;
    
    @Autowired
    private MessageQueuePerformanceOptimizer mqOptimizer;

    /**
     * 获取综合性能报告
     */
    @GetMapping("/report/comprehensive")
    public Map<String, Object> getComprehensiveReport() {
        Map<String, Object> report = new HashMap<>();
        
        // 基础性能报告
        report.put("basic", performanceOptimizer.analyzeAndOptimize());
        
        // 缓存性能报告
        report.put("cache", cacheOptimizer.analyzeAndOptimize());
        
        // 数据库性能报告
        report.put("database", databaseOptimizer.analyzeAndOptimize());
        
        // 消息队列性能报告
        report.put("messageQueue", mqOptimizer.analyzeAndOptimize());
        
        // 系统整体指标
        report.put("systemMetrics", performanceMonitor.getSystemMetrics());
        
        return report;
    }

    /**
     * 获取性能监控状态
     */
    @GetMapping("/monitor/status")
    public Map<String, Object> getMonitorStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("isMonitoring", performanceMonitor.isMonitoring());
        status.put("systemMetrics", performanceMonitor.getSystemMetrics());
        status.put("methodStats", performanceMonitor.getMethodStats());
        status.put("apiStats", performanceMonitor.getApiStats());
        status.put("errorStats", performanceMonitor.getErrorStats());
        return status;
    }

    /**
     * 开始性能监控
     */
    @PostMapping("/monitor/start")
    public Map<String, Object> startMonitoring() {
        performanceMonitor.startMonitoring();
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "性能监控已开始");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    /**
     * 停止性能监控
     */
    @PostMapping("/monitor/stop")
    public Map<String, Object> stopMonitoring() {
        performanceMonitor.stopMonitoring();
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "性能监控已停止");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    /**
     * 清空监控数据
     */
    @PostMapping("/monitor/clear")
    public Map<String, Object> clearMonitorData() {
        performanceMonitor.clearStats();
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "监控数据已清空");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    /**
     * 执行压力测试
     */
    @PostMapping("/test/stress")
    public CompletableFuture<PerformanceTester.StressTestResult> executeStressTest(
            @RequestBody PerformanceTester.StressTestConfig config) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return performanceTester.executeStressTest(config);
            } catch (Exception e) {
                throw new RuntimeException("压力测试执行失败", e);
            }
        });
    }

    /**
     * 执行基准测试
     */
    @PostMapping("/test/benchmark")
    public CompletableFuture<PerformanceTester.BenchmarkResult> executeBenchmark(
            @RequestBody PerformanceTester.BenchmarkConfig config) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return performanceTester.executeBenchmark(config);
            } catch (Exception e) {
                throw new RuntimeException("基准测试执行失败", e);
            }
        });
    }

    /**
     * 执行并发测试
     */
    @PostMapping("/test/concurrency")
    public CompletableFuture<PerformanceTester.ConcurrencyTestResult> executeConcurrencyTest(
            @RequestBody PerformanceTester.ConcurrencyTestConfig config) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return performanceTester.executeConcurrencyTest(config);
            } catch (Exception e) {
                throw new RuntimeException("并发测试执行失败", e);
            }
        });
    }

    /**
     * 获取缓存性能分析
     */
    @GetMapping("/cache/analysis")
    public CachePerformanceOptimizer.CacheOptimizationReport getCacheAnalysis() {
        return cacheOptimizer.analyzeAndOptimize();
    }

    /**
     * 获取数据库性能分析
     */
    @GetMapping("/database/analysis")
    public DatabasePerformanceOptimizer.DatabaseOptimizationReport getDatabaseAnalysis() {
        return databaseOptimizer.analyzeAndOptimize();
    }

    /**
     * 获取消息队列性能分析
     */
    @GetMapping("/mq/analysis")
    public MessageQueuePerformanceOptimizer.MqOptimizationReport getMqAnalysis() {
        return mqOptimizer.analyzeAndOptimize();
    }

    /**
     * 记录缓存访问
     */
    @PostMapping("/cache/record")
    public Map<String, Object> recordCacheAccess(
            @RequestParam String cacheName,
            @RequestParam String key,
            @RequestParam boolean hit,
            @RequestParam long accessTime,
            @RequestParam(required = false, defaultValue = "0") int size) {
        cacheOptimizer.recordCacheAccess(cacheName, key, hit, accessTime, size);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "缓存访问记录成功");
        return result;
    }

    /**
     * 记录SQL执行
     */
    @PostMapping("/database/record/sql")
    public Map<String, Object> recordSqlExecution(
            @RequestParam String sql,
            @RequestParam long executionTime,
            @RequestParam int affectedRows,
            @RequestParam(required = false) String error) {
        databaseOptimizer.recordSqlExecution(sql, executionTime, affectedRows, error);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "SQL执行记录成功");
        return result;
    }

    /**
     * 记录消息发送
     */
    @PostMapping("/mq/record/send")
    public Map<String, Object> recordMessageSend(
            @RequestParam String topic,
            @RequestParam(required = false) String tag,
            @RequestParam long sendTime,
            @RequestParam boolean success,
            @RequestParam int messageSize) {
        mqOptimizer.recordMessageSend(topic, tag, sendTime, success, messageSize);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "消息发送记录成功");
        return result;
    }

    /**
     * 记录消息消费
     */
    @PostMapping("/mq/record/consume")
    public Map<String, Object> recordMessageConsume(
            @RequestParam String topic,
            @RequestParam(required = false) String tag,
            @RequestParam String consumerGroup,
            @RequestParam long consumeTime,
            @RequestParam boolean success,
            @RequestParam long messageAge) {
        mqOptimizer.recordMessageConsume(topic, tag, consumerGroup, consumeTime, success, messageAge);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "消息消费记录成功");
        return result;
    }

    /**
     * 获取系统健康评分
     */
    @GetMapping("/health/score")
    public Map<String, Object> getSystemHealthScore() {
        Map<String, Object> healthScore = new HashMap<>();
        
        // 获取各模块优化报告
        PerformanceOptimizer.OptimizationReport basicReport = performanceOptimizer.analyzeAndOptimize();
        CachePerformanceOptimizer.CacheOptimizationReport cacheReport = cacheOptimizer.analyzeAndOptimize();
        DatabasePerformanceOptimizer.DatabaseOptimizationReport dbReport = databaseOptimizer.analyzeAndOptimize();
        MessageQueuePerformanceOptimizer.MqOptimizationReport mqReport = mqOptimizer.analyzeAndOptimize();
        
        // 计算各模块评分
        healthScore.put("basicScore", basicReport.getOverallScore());
        healthScore.put("cacheScore", cacheReport.getOverallScore());
        healthScore.put("databaseScore", dbReport.getOverallScore());
        healthScore.put("mqScore", mqReport.getOverallScore());
        
        // 计算综合评分（加权平均）
        double overallScore = (basicReport.getOverallScore() * 0.3 +
                              cacheReport.getOverallScore() * 0.25 +
                              dbReport.getOverallScore() * 0.25 +
                              mqReport.getOverallScore() * 0.2);
        
        healthScore.put("overallScore", Math.round(overallScore));
        healthScore.put("level", getHealthLevel((int) Math.round(overallScore)));
        healthScore.put("timestamp", System.currentTimeMillis());
        
        return healthScore;
    }

    /**
     * 获取性能趋势数据
     */
    @GetMapping("/trend")
    public Map<String, Object> getPerformanceTrend(
            @RequestParam(required = false, defaultValue = "24") int hours) {
        Map<String, Object> trend = new HashMap<>();
        
        // 这里可以从时序数据库或缓存中获取历史数据
        // 暂时返回当前状态
        trend.put("systemMetrics", performanceMonitor.getSystemMetrics());
        trend.put("cacheMetrics", cacheOptimizer.getStats());
        trend.put("databaseMetrics", databaseOptimizer.getStats());
        trend.put("mqMetrics", mqOptimizer.getStats());
        trend.put("timeRange", hours + " hours");
        trend.put("timestamp", System.currentTimeMillis());
        
        return trend;
    }

    /**
     * 执行自动优化
     */
    @PostMapping("/auto-optimize")
    public CompletableFuture<Map<String, Object>> executeAutoOptimization() {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, Object> result = new HashMap<>();
            
            try {
                // 执行各模块的自动优化
                PerformanceOptimizer.OptimizationReport basicReport = performanceOptimizer.analyzeAndOptimize();
                CachePerformanceOptimizer.CacheOptimizationReport cacheReport = cacheOptimizer.analyzeAndOptimize();
                DatabasePerformanceOptimizer.DatabaseOptimizationReport dbReport = databaseOptimizer.analyzeAndOptimize();
                MessageQueuePerformanceOptimizer.MqOptimizationReport mqReport = mqOptimizer.analyzeAndOptimize();
                
                // 统计优化建议数量
                int totalSuggestions = basicReport.getSuggestions().size() +
                                     cacheReport.getSuggestions().size() +
                                     dbReport.getSuggestions().size() +
                                     mqReport.getSuggestions().size();
                
                result.put("success", true);
                result.put("message", "自动优化分析完成");
                result.put("totalSuggestions", totalSuggestions);
                result.put("basicSuggestions", basicReport.getSuggestions().size());
                result.put("cacheSuggestions", cacheReport.getSuggestions().size());
                result.put("databaseSuggestions", dbReport.getSuggestions().size());
                result.put("mqSuggestions", mqReport.getSuggestions().size());
                result.put("timestamp", System.currentTimeMillis());
                
            } catch (Exception e) {
                result.put("success", false);
                result.put("message", "自动优化执行失败: " + e.getMessage());
                result.put("timestamp", System.currentTimeMillis());
            }
            
            return result;
        });
    }

    /**
     * 获取优化建议摘要
     */
    @GetMapping("/suggestions/summary")
    public Map<String, Object> getOptimizationSummary() {
        Map<String, Object> summary = new HashMap<>();
        
        // 获取各模块优化报告
        PerformanceOptimizer.OptimizationReport basicReport = performanceOptimizer.analyzeAndOptimize();
        CachePerformanceOptimizer.CacheOptimizationReport cacheReport = cacheOptimizer.analyzeAndOptimize();
        DatabasePerformanceOptimizer.DatabaseOptimizationReport dbReport = databaseOptimizer.analyzeAndOptimize();
        MessageQueuePerformanceOptimizer.MqOptimizationReport mqReport = mqOptimizer.analyzeAndOptimize();
        
        // 统计各优先级建议数量
        Map<String, Integer> priorityCount = new HashMap<>();
        priorityCount.put("HIGH", 0);
        priorityCount.put("MEDIUM", 0);
        priorityCount.put("LOW", 0);
        
        // 统计基础性能建议
        basicReport.getSuggestions().forEach(s -> 
            priorityCount.merge(s.getPriority(), 1, Integer::sum));
        
        // 统计缓存建议
        cacheReport.getSuggestions().forEach(s -> 
            priorityCount.merge(s.getPriority(), 1, Integer::sum));
        
        // 统计数据库建议
        dbReport.getSuggestions().forEach(s -> 
            priorityCount.merge(s.getPriority(), 1, Integer::sum));
        
        // 统计消息队列建议
        mqReport.getSuggestions().forEach(s -> 
            priorityCount.merge(s.getPriority(), 1, Integer::sum));
        
        summary.put("priorityDistribution", priorityCount);
        summary.put("totalSuggestions", priorityCount.values().stream().mapToInt(Integer::intValue).sum());
        summary.put("moduleBreakdown", Map.of(
            "basic", basicReport.getSuggestions().size(),
            "cache", cacheReport.getSuggestions().size(),
            "database", dbReport.getSuggestions().size(),
            "messageQueue", mqReport.getSuggestions().size()
        ));
        summary.put("timestamp", System.currentTimeMillis());
        
        return summary;
    }

    /**
     * 清空所有性能数据
     */
    @PostMapping("/clear-all")
    public Map<String, Object> clearAllPerformanceData() {
        performanceMonitor.clearStats();
        cacheOptimizer.clearStats();
        databaseOptimizer.clearStats();
        mqOptimizer.clearStats();
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "所有性能数据已清空");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    /**
     * 获取健康等级
     */
    private String getHealthLevel(int score) {
        if (score >= 90) {
            return "优秀";
        } else if (score >= 80) {
            return "良好";
        } else if (score >= 70) {
            return "一般";
        } else if (score >= 60) {
            return "较差";
        } else {
            return "差";
        }
    }

    /**
     * 异常处理
     */
    @ExceptionHandler(Exception.class)
    public Map<String, Object> handleException(Exception e) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", "操作失败: " + e.getMessage());
        error.put("timestamp", System.currentTimeMillis());
        return error;
    }
}