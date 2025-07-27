package com.chennian.storytelling.common.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 缓存性能优化器
 * 分析缓存使用情况并提供优化建议
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Component
public class CachePerformanceOptimizer {

    @Autowired
    private MultiLevelCacheManager cacheManager;

    // 缓存访问统计
    private final Map<String, CacheAccessStats> accessStats = new ConcurrentHashMap<>();
    
    // 缓存热点数据统计
    private final Map<String, Map<String, AtomicLong>> hotKeyStats = new ConcurrentHashMap<>();
    
    // 缓存大小统计
    private final Map<String, CacheSizeStats> sizeStats = new ConcurrentHashMap<>();

    /**
     * 记录缓存访问
     */
    public void recordCacheAccess(String cacheName, String key, boolean hit, long accessTime) {
        // 更新访问统计
        accessStats.computeIfAbsent(cacheName, k -> new CacheAccessStats())
                   .recordAccess(hit, accessTime);
        
        // 更新热点统计
        hotKeyStats.computeIfAbsent(cacheName, k -> new ConcurrentHashMap<>())
                   .computeIfAbsent(key, k -> new AtomicLong(0))
                   .incrementAndGet();
    }

    /**
     * 记录缓存大小变化
     */
    public void recordCacheSize(String cacheName, long l1Size, long l2Size, long totalMemory) {
        sizeStats.put(cacheName, new CacheSizeStats(l1Size, l2Size, totalMemory, System.currentTimeMillis()));
    }

    /**
     * 分析缓存性能并生成优化建议
     */
    public CacheOptimizationReport analyzeAndOptimize() {
        List<CacheOptimizationSuggestion> suggestions = new ArrayList<>();
        Map<String, CachePerformanceMetrics> metricsMap = new HashMap<>();

        // 分析每个缓存的性能
        for (Map.Entry<String, CacheAccessStats> entry : accessStats.entrySet()) {
            String cacheName = entry.getKey();
            CacheAccessStats stats = entry.getValue();
            
            // 计算性能指标
            CachePerformanceMetrics metrics = calculateMetrics(cacheName, stats);
            metricsMap.put(cacheName, metrics);
            
            // 生成优化建议
            suggestions.addAll(generateSuggestions(cacheName, metrics));
        }

        // 分析热点数据
        suggestions.addAll(analyzeHotKeys());
        
        // 分析缓存大小
        suggestions.addAll(analyzeCacheSize());

        return new CacheOptimizationReport(
                metricsMap,
                suggestions,
                calculateOverallScore(metricsMap),
                System.currentTimeMillis()
        );
    }

    /**
     * 计算缓存性能指标
     */
    private CachePerformanceMetrics calculateMetrics(String cacheName, CacheAccessStats stats) {
        double hitRate = stats.getTotalAccess() > 0 ? 
                (double) stats.getHitCount() / stats.getTotalAccess() * 100 : 0;
        
        double avgAccessTime = stats.getTotalAccess() > 0 ? 
                (double) stats.getTotalAccessTime() / stats.getTotalAccess() : 0;
        
        CacheSizeStats sizeStats = this.sizeStats.get(cacheName);
        long totalSize = sizeStats != null ? sizeStats.getL1Size() + sizeStats.getL2Size() : 0;
        
        return new CachePerformanceMetrics(
                cacheName,
                hitRate,
                stats.getHitCount(),
                stats.getMissCount(),
                stats.getTotalAccess(),
                avgAccessTime,
                totalSize,
                sizeStats != null ? sizeStats.getTotalMemory() : 0
        );
    }

    /**
     * 生成优化建议
     */
    private List<CacheOptimizationSuggestion> generateSuggestions(String cacheName, CachePerformanceMetrics metrics) {
        List<CacheOptimizationSuggestion> suggestions = new ArrayList<>();

        // 命中率过低
        if (metrics.getHitRate() < 70) {
            suggestions.add(new CacheOptimizationSuggestion(
                    "LOW_HIT_RATE",
                    "缓存命中率过低",
                    String.format("缓存 %s 的命中率仅为 %.2f%%，建议检查缓存策略", cacheName, metrics.getHitRate()),
                    "HIGH",
                    Arrays.asList(
                            "增加缓存容量",
                            "优化缓存键设计",
                            "调整过期时间",
                            "预热热点数据"
                    )
            ));
        }

        // 访问时间过长
        if (metrics.getAvgAccessTime() > 50) {
            suggestions.add(new CacheOptimizationSuggestion(
                    "HIGH_ACCESS_TIME",
                    "缓存访问时间过长",
                    String.format("缓存 %s 的平均访问时间为 %.2fms，建议优化", cacheName, metrics.getAvgAccessTime()),
                    "MEDIUM",
                    Arrays.asList(
                            "优化序列化方式",
                            "减少网络延迟",
                            "使用本地缓存",
                            "优化数据结构"
                    )
            ));
        }

        // 内存使用率过高
        if (metrics.getTotalMemory() > 0 && (double) metrics.getTotalSize() / metrics.getTotalMemory() > 0.8) {
            suggestions.add(new CacheOptimizationSuggestion(
                    "HIGH_MEMORY_USAGE",
                    "内存使用率过高",
                    String.format("缓存 %s 的内存使用率超过80%%，建议清理或扩容", cacheName),
                    "HIGH",
                    Arrays.asList(
                            "清理过期数据",
                            "增加内存容量",
                            "优化数据压缩",
                            "调整淘汰策略"
                    )
            ));
        }

        return suggestions;
    }

    /**
     * 分析热点数据
     */
    private List<CacheOptimizationSuggestion> analyzeHotKeys() {
        List<CacheOptimizationSuggestion> suggestions = new ArrayList<>();

        for (Map.Entry<String, Map<String, AtomicLong>> cacheEntry : hotKeyStats.entrySet()) {
            String cacheName = cacheEntry.getKey();
            Map<String, AtomicLong> keyStats = cacheEntry.getValue();

            // 找出访问频率最高的键
            List<Map.Entry<String, AtomicLong>> sortedKeys = keyStats.entrySet().stream()
                    .sorted(Map.Entry.<String, AtomicLong>comparingByValue(
                            (a, b) -> Long.compare(b.get(), a.get())))
                    .limit(10)
                    .collect(Collectors.toList());

            if (!sortedKeys.isEmpty()) {
                long totalAccess = keyStats.values().stream().mapToLong(AtomicLong::get).sum();
                long topKeyAccess = sortedKeys.get(0).getValue().get();
                
                // 如果某个键的访问量占总访问量的20%以上，认为是热点
                if ((double) topKeyAccess / totalAccess > 0.2) {
                    suggestions.add(new CacheOptimizationSuggestion(
                            "HOT_KEY_DETECTED",
                            "检测到热点数据",
                            String.format("缓存 %s 中检测到热点键: %s，访问量占比 %.2f%%", 
                                    cacheName, sortedKeys.get(0).getKey(), 
                                    (double) topKeyAccess / totalAccess * 100),
                            "MEDIUM",
                            Arrays.asList(
                                    "使用本地缓存预热热点数据",
                                    "增加热点数据的缓存时间",
                                    "考虑数据分片",
                                    "使用CDN缓存"
                            )
                    ));
                }
            }
        }

        return suggestions;
    }

    /**
     * 分析缓存大小
     */
    private List<CacheOptimizationSuggestion> analyzeCacheSize() {
        List<CacheOptimizationSuggestion> suggestions = new ArrayList<>();

        for (Map.Entry<String, CacheSizeStats> entry : sizeStats.entrySet()) {
            String cacheName = entry.getKey();
            CacheSizeStats stats = entry.getValue();

            // L1缓存过大
            if (stats.getL1Size() > 10000) {
                suggestions.add(new CacheOptimizationSuggestion(
                        "L1_CACHE_TOO_LARGE",
                        "L1缓存过大",
                        String.format("缓存 %s 的L1缓存大小为 %d，可能影响性能", cacheName, stats.getL1Size()),
                        "MEDIUM",
                        Arrays.asList(
                                "减少L1缓存容量",
                                "优化淘汰策略",
                                "增加L2缓存使用"
                        )
                ));
            }

            // L1/L2比例不合理
            if (stats.getL2Size() > 0) {
                double l1Ratio = (double) stats.getL1Size() / (stats.getL1Size() + stats.getL2Size());
                if (l1Ratio < 0.1 || l1Ratio > 0.9) {
                    suggestions.add(new CacheOptimizationSuggestion(
                            "UNBALANCED_CACHE_RATIO",
                            "缓存层级比例不合理",
                            String.format("缓存 %s 的L1/L2比例为 %.2f，建议调整", cacheName, l1Ratio),
                            "LOW",
                            Arrays.asList(
                                    "调整L1缓存容量",
                                    "优化缓存分层策略",
                                    "重新评估数据访问模式"
                            )
                    ));
                }
            }
        }

        return suggestions;
    }

    /**
     * 计算整体评分
     */
    private int calculateOverallScore(Map<String, CachePerformanceMetrics> metricsMap) {
        if (metricsMap.isEmpty()) {
            return 100;
        }

        double totalScore = 0;
        for (CachePerformanceMetrics metrics : metricsMap.values()) {
            double score = 0;
            
            // 命中率权重40%
            score += Math.min(metrics.getHitRate(), 100) * 0.4;
            
            // 访问时间权重30% (越小越好)
            double timeScore = Math.max(0, 100 - metrics.getAvgAccessTime());
            score += timeScore * 0.3;
            
            // 内存使用率权重30%
            if (metrics.getTotalMemory() > 0) {
                double memoryUsage = (double) metrics.getTotalSize() / metrics.getTotalMemory();
                double memoryScore = Math.max(0, 100 - memoryUsage * 100);
                score += memoryScore * 0.3;
            } else {
                score += 30; // 如果没有内存信息，给予中等分数
            }
            
            totalScore += score;
        }

        return (int) (totalScore / metricsMap.size());
    }

    /**
     * 获取缓存统计信息
     */
    public Map<String, CacheAccessStats> getCacheStats() {
        return new HashMap<>(accessStats);
    }

    /**
     * 获取热点数据统计
     */
    public Map<String, List<HotKeyInfo>> getHotKeys(int topN) {
        Map<String, List<HotKeyInfo>> result = new HashMap<>();
        
        for (Map.Entry<String, Map<String, AtomicLong>> cacheEntry : hotKeyStats.entrySet()) {
            String cacheName = cacheEntry.getKey();
            List<HotKeyInfo> hotKeys = cacheEntry.getValue().entrySet().stream()
                    .sorted(Map.Entry.<String, AtomicLong>comparingByValue(
                            (a, b) -> Long.compare(b.get(), a.get())))
                    .limit(topN)
                    .map(entry -> new HotKeyInfo(entry.getKey(), entry.getValue().get()))
                    .collect(Collectors.toList());
            
            result.put(cacheName, hotKeys);
        }
        
        return result;
    }

    /**
     * 清空统计数据
     */
    public void clearStats() {
        accessStats.clear();
        hotKeyStats.clear();
        sizeStats.clear();
    }

    // 内部类定义
    
    /**
     * 缓存访问统计
     */
    public static class CacheAccessStats {
        private final AtomicLong hitCount = new AtomicLong(0);
        private final AtomicLong missCount = new AtomicLong(0);
        private final AtomicLong totalAccessTime = new AtomicLong(0);

        public void recordAccess(boolean hit, long accessTime) {
            if (hit) {
                hitCount.incrementAndGet();
            } else {
                missCount.incrementAndGet();
            }
            totalAccessTime.addAndGet(accessTime);
        }

        public long getHitCount() { return hitCount.get(); }
        public long getMissCount() { return missCount.get(); }
        public long getTotalAccess() { return hitCount.get() + missCount.get(); }
        public long getTotalAccessTime() { return totalAccessTime.get(); }
    }

    /**
     * 缓存大小统计
     */
    public static class CacheSizeStats {
        private final long l1Size;
        private final long l2Size;
        private final long totalMemory;
        private final long timestamp;

        public CacheSizeStats(long l1Size, long l2Size, long totalMemory, long timestamp) {
            this.l1Size = l1Size;
            this.l2Size = l2Size;
            this.totalMemory = totalMemory;
            this.timestamp = timestamp;
        }

        public long getL1Size() { return l1Size; }
        public long getL2Size() { return l2Size; }
        public long getTotalMemory() { return totalMemory; }
        public long getTimestamp() { return timestamp; }
    }

    /**
     * 缓存性能指标
     */
    public static class CachePerformanceMetrics {
        private final String cacheName;
        private final double hitRate;
        private final long hitCount;
        private final long missCount;
        private final long totalAccess;
        private final double avgAccessTime;
        private final long totalSize;
        private final long totalMemory;

        public CachePerformanceMetrics(String cacheName, double hitRate, long hitCount, 
                                     long missCount, long totalAccess, double avgAccessTime,
                                     long totalSize, long totalMemory) {
            this.cacheName = cacheName;
            this.hitRate = hitRate;
            this.hitCount = hitCount;
            this.missCount = missCount;
            this.totalAccess = totalAccess;
            this.avgAccessTime = avgAccessTime;
            this.totalSize = totalSize;
            this.totalMemory = totalMemory;
        }

        // Getters
        public String getCacheName() { return cacheName; }
        public double getHitRate() { return hitRate; }
        public long getHitCount() { return hitCount; }
        public long getMissCount() { return missCount; }
        public long getTotalAccess() { return totalAccess; }
        public double getAvgAccessTime() { return avgAccessTime; }
        public long getTotalSize() { return totalSize; }
        public long getTotalMemory() { return totalMemory; }
    }

    /**
     * 缓存优化建议
     */
    public static class CacheOptimizationSuggestion {
        private final String type;
        private final String title;
        private final String description;
        private final String priority;
        private final List<String> actions;

        public CacheOptimizationSuggestion(String type, String title, String description, 
                                          String priority, List<String> actions) {
            this.type = type;
            this.title = title;
            this.description = description;
            this.priority = priority;
            this.actions = actions;
        }

        // Getters
        public String getType() { return type; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getPriority() { return priority; }
        public List<String> getActions() { return actions; }
    }

    /**
     * 缓存优化报告
     */
    public static class CacheOptimizationReport {
        private final Map<String, CachePerformanceMetrics> metrics;
        private final List<CacheOptimizationSuggestion> suggestions;
        private final int overallScore;
        private final long timestamp;

        public CacheOptimizationReport(Map<String, CachePerformanceMetrics> metrics,
                                     List<CacheOptimizationSuggestion> suggestions,
                                     int overallScore, long timestamp) {
            this.metrics = metrics;
            this.suggestions = suggestions;
            this.overallScore = overallScore;
            this.timestamp = timestamp;
        }

        // Getters
        public Map<String, CachePerformanceMetrics> getMetrics() { return metrics; }
        public List<CacheOptimizationSuggestion> getSuggestions() { return suggestions; }
        public int getOverallScore() { return overallScore; }
        public long getTimestamp() { return timestamp; }
    }

    /**
     * 热点数据信息
     */
    public static class HotKeyInfo {
        private final String key;
        private final long accessCount;

        public HotKeyInfo(String key, long accessCount) {
            this.key = key;
            this.accessCount = accessCount;
        }

        public String getKey() { return key; }
        public long getAccessCount() { return accessCount; }
    }
}