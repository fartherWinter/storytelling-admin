package com.chennian.storytelling.common.performance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 性能优化器
 * 分析性能数据并提供优化建议
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Component
public class PerformanceOptimizer {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceOptimizer.class);

    @Autowired
    private PerformanceMonitor performanceMonitor;

    // 性能阈值配置
    private static final double HIGH_ERROR_RATE_THRESHOLD = 0.05; // 5%
    private static final long SLOW_RESPONSE_TIME_THRESHOLD = 1000; // 1秒
    private static final double HIGH_MEMORY_USAGE_THRESHOLD = 0.8; // 80%
    private static final int HIGH_THREAD_COUNT_THRESHOLD = 200;
    private static final double LOW_THROUGHPUT_THRESHOLD = 10.0; // 10 TPS

    /**
     * 分析性能并生成优化建议
     * 
     * @return 优化建议列表
     */
    public List<OptimizationSuggestion> analyzeAndSuggest() {
        List<OptimizationSuggestion> suggestions = new ArrayList<>();
        
        PerformanceMonitor.PerformanceReport report = performanceMonitor.getPerformanceReport();
        
        // 分析系统指标
        suggestions.addAll(analyzeSystemMetrics(report.getSystemMetrics()));
        
        // 分析方法性能
        suggestions.addAll(analyzeMethodMetrics(report.getMethodMetrics()));
        
        // 分析API性能
        suggestions.addAll(analyzeApiMetrics(report.getApiMetrics()));
        
        // 按优先级排序
        suggestions.sort((s1, s2) -> s2.getPriority().compareTo(s1.getPriority()));
        
        logger.info("Generated {} optimization suggestions", suggestions.size());
        return suggestions;
    }

    /**
     * 分析系统指标
     */
    private List<OptimizationSuggestion> analyzeSystemMetrics(PerformanceMonitor.SystemMetrics metrics) {
        List<OptimizationSuggestion> suggestions = new ArrayList<>();
        
        // 内存使用率分析
        if (metrics.getHeapUsagePercentage() > HIGH_MEMORY_USAGE_THRESHOLD * 100) {
            suggestions.add(new OptimizationSuggestion(
                    OptimizationSuggestion.Priority.HIGH,
                    OptimizationSuggestion.Category.MEMORY,
                    "高内存使用率",
                    String.format("当前堆内存使用率为 %.2f%%，超过阈值 %.0f%%", 
                                 metrics.getHeapUsagePercentage(), HIGH_MEMORY_USAGE_THRESHOLD * 100),
                    List.of(
                            "增加JVM堆内存大小 (-Xmx)",
                            "优化对象创建，减少不必要的对象分配",
                            "检查是否存在内存泄漏",
                            "启用G1GC或ZGC等低延迟垃圾收集器",
                            "增加缓存过期策略，及时释放缓存对象"
                    )
            ));
        }
        
        // 线程数分析
        if (metrics.getThreadCount() > HIGH_THREAD_COUNT_THRESHOLD) {
            suggestions.add(new OptimizationSuggestion(
                    OptimizationSuggestion.Priority.MEDIUM,
                    OptimizationSuggestion.Category.THREADING,
                    "线程数过多",
                    String.format("当前线程数为 %d，超过建议阈值 %d", 
                                 metrics.getThreadCount(), HIGH_THREAD_COUNT_THRESHOLD),
                    List.of(
                            "优化线程池配置，减少不必要的线程创建",
                            "使用异步处理减少阻塞线程",
                            "检查是否存在线程泄漏",
                            "合并相似功能的线程池",
                            "使用虚拟线程（Java 19+）"
                    )
            ));
        }
        
        return suggestions;
    }

    /**
     * 分析方法性能指标
     */
    private List<OptimizationSuggestion> analyzeMethodMetrics(Map<String, PerformanceMonitor.MethodMetrics> methodMetrics) {
        List<OptimizationSuggestion> suggestions = new ArrayList<>();
        
        // 找出高错误率的方法
        List<Map.Entry<String, PerformanceMonitor.MethodMetrics>> highErrorRateMethods = 
                methodMetrics.entrySet().stream()
                        .filter(entry -> entry.getValue().getErrorRate() > HIGH_ERROR_RATE_THRESHOLD)
                        .collect(Collectors.toList());
        
        if (!highErrorRateMethods.isEmpty()) {
            suggestions.add(new OptimizationSuggestion(
                    OptimizationSuggestion.Priority.HIGH,
                    OptimizationSuggestion.Category.ERROR_HANDLING,
                    "方法错误率过高",
                    String.format("发现 %d 个方法错误率超过 %.0f%%: %s", 
                                 highErrorRateMethods.size(), 
                                 HIGH_ERROR_RATE_THRESHOLD * 100,
                                 highErrorRateMethods.stream()
                                         .map(Map.Entry::getKey)
                                         .collect(Collectors.joining(", "))),
                    List.of(
                            "增加异常处理和重试机制",
                            "检查输入参数验证",
                            "优化外部依赖调用的超时和熔断",
                            "增加详细的错误日志记录",
                            "考虑降级策略"
                    )
            ));
        }
        
        // 找出响应时间过长的方法
        List<Map.Entry<String, PerformanceMonitor.MethodMetrics>> slowMethods = 
                methodMetrics.entrySet().stream()
                        .filter(entry -> entry.getValue().getAverageTime() > SLOW_RESPONSE_TIME_THRESHOLD)
                        .collect(Collectors.toList());
        
        if (!slowMethods.isEmpty()) {
            suggestions.add(new OptimizationSuggestion(
                    OptimizationSuggestion.Priority.MEDIUM,
                    OptimizationSuggestion.Category.PERFORMANCE,
                    "方法响应时间过长",
                    String.format("发现 %d 个方法平均响应时间超过 %d ms: %s", 
                                 slowMethods.size(), 
                                 SLOW_RESPONSE_TIME_THRESHOLD,
                                 slowMethods.stream()
                                         .map(entry -> entry.getKey() + "(" + String.format("%.0f", entry.getValue().getAverageTime()) + "ms)")
                                         .collect(Collectors.joining(", "))),
                    List.of(
                            "优化数据库查询，添加索引",
                            "增加缓存减少重复计算",
                            "使用异步处理非关键路径",
                            "优化算法复杂度",
                            "考虑分页或批处理大数据量操作"
                    )
            ));
        }
        
        return suggestions;
    }

    /**
     * 分析API性能指标
     */
    private List<OptimizationSuggestion> analyzeApiMetrics(Map<String, PerformanceMonitor.ApiMetrics> apiMetrics) {
        List<OptimizationSuggestion> suggestions = new ArrayList<>();
        
        // 找出低成功率的API
        List<Map.Entry<String, PerformanceMonitor.ApiMetrics>> lowSuccessRateApis = 
                apiMetrics.entrySet().stream()
                        .filter(entry -> entry.getValue().getSuccessRate() < (1 - HIGH_ERROR_RATE_THRESHOLD))
                        .collect(Collectors.toList());
        
        if (!lowSuccessRateApis.isEmpty()) {
            suggestions.add(new OptimizationSuggestion(
                    OptimizationSuggestion.Priority.HIGH,
                    OptimizationSuggestion.Category.API,
                    "API成功率过低",
                    String.format("发现 %d 个API成功率低于 %.0f%%: %s", 
                                 lowSuccessRateApis.size(), 
                                 (1 - HIGH_ERROR_RATE_THRESHOLD) * 100,
                                 lowSuccessRateApis.stream()
                                         .map(entry -> entry.getKey() + "(" + String.format("%.1f", entry.getValue().getSuccessRate() * 100) + "%)")
                                         .collect(Collectors.joining(", "))),
                    List.of(
                            "增加API输入参数校验",
                            "优化错误处理和响应格式",
                            "增加API限流和熔断保护",
                            "检查依赖服务的稳定性",
                            "增加API监控和告警"
                    )
            ));
        }
        
        // 找出响应时间过长的API
        List<Map.Entry<String, PerformanceMonitor.ApiMetrics>> slowApis = 
                apiMetrics.entrySet().stream()
                        .filter(entry -> entry.getValue().getAverageResponseTime() > SLOW_RESPONSE_TIME_THRESHOLD)
                        .collect(Collectors.toList());
        
        if (!slowApis.isEmpty()) {
            suggestions.add(new OptimizationSuggestion(
                    OptimizationSuggestion.Priority.MEDIUM,
                    OptimizationSuggestion.Category.API,
                    "API响应时间过长",
                    String.format("发现 %d 个API平均响应时间超过 %d ms: %s", 
                                 slowApis.size(), 
                                 SLOW_RESPONSE_TIME_THRESHOLD,
                                 slowApis.stream()
                                         .map(entry -> entry.getKey() + "(" + String.format("%.0f", entry.getValue().getAverageResponseTime()) + "ms)")
                                         .collect(Collectors.joining(", "))),
                    List.of(
                            "优化数据库查询性能",
                            "增加响应数据缓存",
                            "减少不必要的数据传输",
                            "使用CDN加速静态资源",
                            "考虑API响应数据压缩"
                    )
            ));
        }
        
        return suggestions;
    }

    /**
     * 生成性能优化报告
     * 
     * @return 优化报告
     */
    public OptimizationReport generateOptimizationReport() {
        List<OptimizationSuggestion> suggestions = analyzeAndSuggest();
        PerformanceMonitor.PerformanceReport performanceReport = performanceMonitor.getPerformanceReport();
        
        return new OptimizationReport(
                System.currentTimeMillis(),
                performanceReport,
                suggestions,
                calculateOverallHealthScore(performanceReport, suggestions)
        );
    }

    /**
     * 计算整体健康评分
     */
    private int calculateOverallHealthScore(PerformanceMonitor.PerformanceReport report, 
                                          List<OptimizationSuggestion> suggestions) {
        int baseScore = 100;
        
        // 根据建议的优先级扣分
        for (OptimizationSuggestion suggestion : suggestions) {
            switch (suggestion.getPriority()) {
                case HIGH:
                    baseScore -= 15;
                    break;
                case MEDIUM:
                    baseScore -= 8;
                    break;
                case LOW:
                    baseScore -= 3;
                    break;
            }
        }
        
        // 根据系统指标调整
        PerformanceMonitor.SystemMetrics systemMetrics = report.getSystemMetrics();
        if (systemMetrics.getHeapUsagePercentage() > 90) {
            baseScore -= 10;
        }
        
        return Math.max(0, Math.min(100, baseScore));
    }

    /**
     * 优化建议
     */
    public static class OptimizationSuggestion {
        public enum Priority {
            HIGH, MEDIUM, LOW
        }
        
        public enum Category {
            MEMORY, THREADING, PERFORMANCE, ERROR_HANDLING, API, DATABASE, CACHE
        }
        
        private final Priority priority;
        private final Category category;
        private final String title;
        private final String description;
        private final List<String> recommendations;
        
        public OptimizationSuggestion(Priority priority, Category category, String title, 
                                     String description, List<String> recommendations) {
            this.priority = priority;
            this.category = category;
            this.title = title;
            this.description = description;
            this.recommendations = recommendations;
        }
        
        // Getters
        public Priority getPriority() { return priority; }
        public Category getCategory() { return category; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public List<String> getRecommendations() { return recommendations; }
        
        @Override
        public String toString() {
            return String.format("OptimizationSuggestion{priority=%s, category=%s, title='%s', description='%s', recommendations=%d}",
                               priority, category, title, description, recommendations.size());
        }
    }

    /**
     * 优化报告
     */
    public static class OptimizationReport {
        private final long timestamp;
        private final PerformanceMonitor.PerformanceReport performanceReport;
        private final List<OptimizationSuggestion> suggestions;
        private final int healthScore;
        
        public OptimizationReport(long timestamp, PerformanceMonitor.PerformanceReport performanceReport,
                                List<OptimizationSuggestion> suggestions, int healthScore) {
            this.timestamp = timestamp;
            this.performanceReport = performanceReport;
            this.suggestions = suggestions;
            this.healthScore = healthScore;
        }
        
        // Getters
        public long getTimestamp() { return timestamp; }
        public PerformanceMonitor.PerformanceReport getPerformanceReport() { return performanceReport; }
        public List<OptimizationSuggestion> getSuggestions() { return suggestions; }
        public int getHealthScore() { return healthScore; }
        
        public long getHighPrioritySuggestions() {
            return suggestions.stream().filter(s -> s.getPriority() == OptimizationSuggestion.Priority.HIGH).count();
        }
        
        public long getMediumPrioritySuggestions() {
            return suggestions.stream().filter(s -> s.getPriority() == OptimizationSuggestion.Priority.MEDIUM).count();
        }
        
        public long getLowPrioritySuggestions() {
            return suggestions.stream().filter(s -> s.getPriority() == OptimizationSuggestion.Priority.LOW).count();
        }
        
        @Override
        public String toString() {
            return String.format("OptimizationReport{timestamp=%d, healthScore=%d, suggestions=%d (H:%d, M:%d, L:%d)}",
                               timestamp, healthScore, suggestions.size(), 
                               getHighPrioritySuggestions(), getMediumPrioritySuggestions(), getLowPrioritySuggestions());
        }
    }
}