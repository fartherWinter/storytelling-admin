package com.chennian.storytelling.admin.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 性能调优建议器
 */
@Component
class PerformanceTuningAdvisor {

    private static final Logger log = LoggerFactory.getLogger(PerformanceTuningAdvisor.class);

    /**
     * 分析性能并提供调优建议
     */
    public List<String> analyzeAndAdvise(Map<String, Object> jvmSnapshot, Map<String, Object> appStats) {
        List<String> recommendations = new ArrayList<>();

        // 分析内存使用
        analyzeMemoryUsage(jvmSnapshot, recommendations);

        // 分析线程使用
        analyzeThreadUsage(jvmSnapshot, recommendations);

        // 分析应用性能
        analyzeApplicationPerformance(appStats, recommendations);

        // 分析GC性能
        analyzeGCPerformance(recommendations);

        return recommendations;
    }

    private void analyzeMemoryUsage(Map<String, Object> snapshot, List<String> recommendations) {
        @SuppressWarnings("unchecked")
        Map<String, Object> memory = (Map<String, Object>) snapshot.get("memory");
        if (memory != null) {
            long heapUsed = (Long) memory.get("heapUsed");
            long heapMax = (Long) memory.get("heapMax");
            double usageRatio = (double) heapUsed / heapMax;

            if (usageRatio > 0.85) {
                recommendations.add("内存使用率过高(" + String.format("%.1f", usageRatio * 100) + "%)，建议增加堆内存大小或优化内存使用");
            }

            if (heapMax < 2L * 1024 * 1024 * 1024) { // 小于2GB
                recommendations.add("堆内存较小，建议根据应用需求适当增加 -Xmx 参数");
            }
        }
    }

    private void analyzeThreadUsage(Map<String, Object> snapshot, List<String> recommendations) {
        @SuppressWarnings("unchecked")
        Map<String, Object> threads = (Map<String, Object>) snapshot.get("threads");
        if (threads != null) {
            int threadCount = (Integer) threads.get("count");
            int peakCount = (Integer) threads.get("peak");

            if (threadCount > 200) {
                recommendations.add("线程数过多(" + threadCount + ")，建议检查线程池配置和异步任务管理");
            }

            if (peakCount > threadCount * 1.5) {
                recommendations.add("线程数波动较大，建议优化线程池配置以减少线程创建开销");
            }
        }
    }

    private void analyzeApplicationPerformance(Map<String, Object> appStats, List<String> recommendations) {
        @SuppressWarnings("unchecked")
        Map<String, Object> operations = (Map<String, Object>) appStats.get("operations");
        if (operations != null) {
            for (Map.Entry<String, Object> entry : operations.entrySet()) {
                String operation = entry.getKey();
                @SuppressWarnings("unchecked")
                Map<String, Object> stats = (Map<String, Object>) entry.getValue();
                double avgTime = (Double) stats.get("avgTimeMs");

                if (avgTime > 1000) { // 超过1秒
                    recommendations.add("操作 '" + operation + "' 平均响应时间过长(" +
                            String.format("%.1f", avgTime) + "ms)，建议进行性能优化");
                }
            }
        }

        @SuppressWarnings("unchecked")
        Map<String, Long> errors = (Map<String, Long>) appStats.get("errors");
        if (errors != null && !errors.isEmpty()) {
            long totalErrors = errors.values().stream().mapToLong(Long::longValue).sum();
            if (totalErrors > 10) {
                recommendations.add("错误数量较多(" + totalErrors + ")，建议检查错误处理和系统稳定性");
            }
        }
    }

    private void analyzeGCPerformance(List<String> recommendations) {
        // 这里可以添加更复杂的GC分析逻辑
        recommendations.add("建议定期监控GC性能，考虑使用G1GC或ZGC以获得更好的延迟表现");
        recommendations.add("建议设置合适的GC参数，如 -XX:+UseG1GC -XX:MaxGCPauseMillis=200");
    }

    /**
     * 生成JVM调优建议
     */
    public List<String> getJVMTuningRecommendations() {
        List<String> recommendations = new ArrayList<>();

        recommendations.add("内存调优: 设置合适的堆内存大小 -Xms2g -Xmx4g");
        recommendations.add("GC调优: 使用G1垃圾收集器 -XX:+UseG1GC -XX:MaxGCPauseMillis=200");
        recommendations.add("线程调优: 设置合适的线程栈大小 -Xss256k");
        recommendations.add("编译优化: 启用分层编译 -XX:+TieredCompilation");
        recommendations.add("监控优化: 启用JFR -XX:+FlightRecorder");
        recommendations.add("安全优化: 禁用不必要的安全管理器以提升性能");

        return recommendations;
    }
}
