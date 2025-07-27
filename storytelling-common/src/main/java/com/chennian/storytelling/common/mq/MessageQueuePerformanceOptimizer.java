package com.chennian.storytelling.common.mq;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 消息队列性能优化器
 * 分析消息队列性能并提供优化建议
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Component
public class MessageQueuePerformanceOptimizer {

    // 消息发送统计
    private final Map<String, MessageSendStats> sendStats = new ConcurrentHashMap<>();
    
    // 消息消费统计
    private final Map<String, MessageConsumeStats> consumeStats = new ConcurrentHashMap<>();
    
    // 队列积压统计
    private final Map<String, QueueBacklogStats> backlogStats = new ConcurrentHashMap<>();
    
    // 消费者性能统计
    private final Map<String, ConsumerPerformanceStats> consumerStats = new ConcurrentHashMap<>();
    
    // 死信队列统计
    private final Map<String, DeadLetterStats> deadLetterStats = new ConcurrentHashMap<>();
    
    // 消息延迟阈值（毫秒）
    private static final long HIGH_LATENCY_THRESHOLD = 5000;
    private static final long MEDIUM_LATENCY_THRESHOLD = 1000;

    /**
     * 记录消息发送
     */
    public void recordMessageSend(String topic, String tag, long sendTime, boolean success, int messageSize) {
        String key = topic + ":" + (tag != null ? tag : "default");
        sendStats.computeIfAbsent(key, k -> new MessageSendStats())
                .recordSend(sendTime, success, messageSize);
    }

    /**
     * 记录消息消费
     */
    public void recordMessageConsume(String topic, String tag, String consumerGroup, 
                                   long consumeTime, boolean success, long messageAge) {
        String key = topic + ":" + (tag != null ? tag : "default") + ":" + consumerGroup;
        consumeStats.computeIfAbsent(key, k -> new MessageConsumeStats())
                   .recordConsume(consumeTime, success, messageAge);
    }

    /**
     * 记录队列积压
     */
    public void recordQueueBacklog(String topic, String consumerGroup, long backlogCount, long oldestMessageAge) {
        String key = topic + ":" + consumerGroup;
        backlogStats.put(key, new QueueBacklogStats(backlogCount, oldestMessageAge, System.currentTimeMillis()));
    }

    /**
     * 记录消费者性能
     */
    public void recordConsumerPerformance(String consumerGroup, int activeConsumers, 
                                        double avgThroughput, double avgCpuUsage, double avgMemoryUsage) {
        consumerStats.put(consumerGroup, new ConsumerPerformanceStats(
                activeConsumers, avgThroughput, avgCpuUsage, avgMemoryUsage, System.currentTimeMillis()));
    }

    /**
     * 记录死信消息
     */
    public void recordDeadLetter(String topic, String consumerGroup, String reason) {
        String key = topic + ":" + consumerGroup;
        deadLetterStats.computeIfAbsent(key, k -> new DeadLetterStats())
                      .recordDeadLetter(reason);
    }

    /**
     * 分析消息队列性能并生成优化报告
     */
    public MqOptimizationReport analyzeAndOptimize() {
        List<MqOptimizationSuggestion> suggestions = new ArrayList<>();
        Map<String, MqPerformanceMetrics> metricsMap = new HashMap<>();

        // 分析发送性能
        suggestions.addAll(analyzeSendPerformance(metricsMap));
        
        // 分析消费性能
        suggestions.addAll(analyzeConsumePerformance(metricsMap));
        
        // 分析队列积压
        suggestions.addAll(analyzeQueueBacklog());
        
        // 分析消费者性能
        suggestions.addAll(analyzeConsumerPerformance());
        
        // 分析死信队列
        suggestions.addAll(analyzeDeadLetters());
        
        // 分析整体架构
        suggestions.addAll(analyzeArchitecture());

        return new MqOptimizationReport(
                metricsMap,
                suggestions,
                getTopBacklogQueues(10),
                getTopDeadLetterQueues(10),
                calculateOverallScore(suggestions),
                System.currentTimeMillis()
        );
    }

    /**
     * 分析发送性能
     */
    private List<MqOptimizationSuggestion> analyzeSendPerformance(
            Map<String, MqPerformanceMetrics> metricsMap) {
        List<MqOptimizationSuggestion> suggestions = new ArrayList<>();

        for (Map.Entry<String, MessageSendStats> entry : sendStats.entrySet()) {
            String topicTag = entry.getKey();
            MessageSendStats stats = entry.getValue();
            
            // 计算性能指标
            MqPerformanceMetrics metrics = new MqPerformanceMetrics(
                    topicTag,
                    "SEND",
                    stats.getAvgSendTime(),
                    stats.getMaxSendTime(),
                    stats.getTotalMessages(),
                    stats.getSuccessRate(),
                    stats.getAvgMessageSize(),
                    0 // 发送没有消费延迟
            );
            metricsMap.put("SEND_" + topicTag, metrics);

            // 生成优化建议
            if (stats.getAvgSendTime() > 100) {
                suggestions.add(new MqOptimizationSuggestion(
                        "SLOW_MESSAGE_SEND",
                        "消息发送延迟过高",
                        String.format("主题 %s 平均发送时间 %.2fms，建议优化", topicTag, stats.getAvgSendTime()),
                        "MEDIUM",
                        Arrays.asList(
                                "使用批量发送",
                                "优化网络配置",
                                "增加生产者实例",
                                "检查序列化性能"
                        ),
                        topicTag
                ));
            }

            if (stats.getSuccessRate() < 99) {
                suggestions.add(new MqOptimizationSuggestion(
                        "LOW_SEND_SUCCESS_RATE",
                        "消息发送成功率过低",
                        String.format("主题 %s 发送成功率 %.2f%%，需要检查", topicTag, stats.getSuccessRate()),
                        "HIGH",
                        Arrays.asList(
                                "检查网络连接",
                                "增加重试机制",
                                "检查Broker状态",
                                "优化消息大小"
                        ),
                        topicTag
                ));
            }

            if (stats.getAvgMessageSize() > 1024 * 1024) { // 1MB
                suggestions.add(new MqOptimizationSuggestion(
                        "LARGE_MESSAGE_SIZE",
                        "消息体过大",
                        String.format("主题 %s 平均消息大小 %.2fKB，建议优化", 
                                topicTag, stats.getAvgMessageSize() / 1024.0),
                        "MEDIUM",
                        Arrays.asList(
                                "压缩消息内容",
                                "拆分大消息",
                                "使用引用传递",
                                "优化数据结构"
                        ),
                        topicTag
                ));
            }
        }

        return suggestions;
    }

    /**
     * 分析消费性能
     */
    private List<MqOptimizationSuggestion> analyzeConsumePerformance(
            Map<String, MqPerformanceMetrics> metricsMap) {
        List<MqOptimizationSuggestion> suggestions = new ArrayList<>();

        for (Map.Entry<String, MessageConsumeStats> entry : consumeStats.entrySet()) {
            String topicTagConsumer = entry.getKey();
            MessageConsumeStats stats = entry.getValue();
            
            // 计算性能指标
            MqPerformanceMetrics metrics = new MqPerformanceMetrics(
                    topicTagConsumer,
                    "CONSUME",
                    stats.getAvgConsumeTime(),
                    stats.getMaxConsumeTime(),
                    stats.getTotalMessages(),
                    stats.getSuccessRate(),
                    0, // 消费没有消息大小
                    stats.getAvgMessageAge()
            );
            metricsMap.put("CONSUME_" + topicTagConsumer, metrics);

            // 生成优化建议
            if (stats.getAvgConsumeTime() > 1000) {
                suggestions.add(new MqOptimizationSuggestion(
                        "SLOW_MESSAGE_CONSUME",
                        "消息消费延迟过高",
                        String.format("消费者 %s 平均消费时间 %.2fms，建议优化", 
                                topicTagConsumer, stats.getAvgConsumeTime()),
                        "HIGH",
                        Arrays.asList(
                                "优化业务逻辑",
                                "增加消费者实例",
                                "使用批量消费",
                                "异步处理非关键逻辑"
                        ),
                        topicTagConsumer
                ));
            }

            if (stats.getSuccessRate() < 95) {
                suggestions.add(new MqOptimizationSuggestion(
                        "LOW_CONSUME_SUCCESS_RATE",
                        "消息消费成功率过低",
                        String.format("消费者 %s 消费成功率 %.2f%%，需要检查", 
                                topicTagConsumer, stats.getSuccessRate()),
                        "HIGH",
                        Arrays.asList(
                                "增强异常处理",
                                "检查业务逻辑",
                                "增加重试机制",
                                "监控资源使用"
                        ),
                        topicTagConsumer
                ));
            }

            if (stats.getAvgMessageAge() > HIGH_LATENCY_THRESHOLD) {
                suggestions.add(new MqOptimizationSuggestion(
                        "HIGH_MESSAGE_LATENCY",
                        "消息延迟过高",
                        String.format("消费者 %s 消息平均延迟 %.2fs，严重影响实时性", 
                                topicTagConsumer, stats.getAvgMessageAge() / 1000.0),
                        "HIGH",
                        Arrays.asList(
                                "增加消费者数量",
                                "优化消费逻辑",
                                "检查网络延迟",
                                "考虑分区扩容"
                        ),
                        topicTagConsumer
                ));
            }
        }

        return suggestions;
    }

    /**
     * 分析队列积压
     */
    private List<MqOptimizationSuggestion> analyzeQueueBacklog() {
        List<MqOptimizationSuggestion> suggestions = new ArrayList<>();

        for (Map.Entry<String, QueueBacklogStats> entry : backlogStats.entrySet()) {
            String topicConsumer = entry.getKey();
            QueueBacklogStats stats = entry.getValue();

            if (stats.getBacklogCount() > 10000) {
                suggestions.add(new MqOptimizationSuggestion(
                        "HIGH_QUEUE_BACKLOG",
                        "队列积压严重",
                        String.format("队列 %s 积压消息 %d 条，需要紧急处理", 
                                topicConsumer, stats.getBacklogCount()),
                        "HIGH",
                        Arrays.asList(
                                "增加消费者实例",
                                "优化消费性能",
                                "临时扩容处理",
                                "检查消费者状态"
                        ),
                        topicConsumer
                ));
            } else if (stats.getBacklogCount() > 1000) {
                suggestions.add(new MqOptimizationSuggestion(
                        "MEDIUM_QUEUE_BACKLOG",
                        "队列积压较多",
                        String.format("队列 %s 积压消息 %d 条，建议关注", 
                                topicConsumer, stats.getBacklogCount()),
                        "MEDIUM",
                        Arrays.asList(
                                "监控消费速度",
                                "评估消费能力",
                                "考虑增加消费者"
                        ),
                        topicConsumer
                ));
            }

            if (stats.getOldestMessageAge() > HIGH_LATENCY_THRESHOLD * 2) {
                suggestions.add(new MqOptimizationSuggestion(
                        "OLD_MESSAGE_IN_QUEUE",
                        "队列中有过期消息",
                        String.format("队列 %s 最老消息已存在 %.2fs，可能影响业务", 
                                topicConsumer, stats.getOldestMessageAge() / 1000.0),
                        "MEDIUM",
                        Arrays.asList(
                                "清理过期消息",
                                "设置消息TTL",
                                "加快消费速度",
                                "检查消息有效性"
                        ),
                        topicConsumer
                ));
            }
        }

        return suggestions;
    }

    /**
     * 分析消费者性能
     */
    private List<MqOptimizationSuggestion> analyzeConsumerPerformance() {
        List<MqOptimizationSuggestion> suggestions = new ArrayList<>();

        for (Map.Entry<String, ConsumerPerformanceStats> entry : consumerStats.entrySet()) {
            String consumerGroup = entry.getKey();
            ConsumerPerformanceStats stats = entry.getValue();

            if (stats.getAvgCpuUsage() > 80) {
                suggestions.add(new MqOptimizationSuggestion(
                        "HIGH_CONSUMER_CPU_USAGE",
                        "消费者CPU使用率过高",
                        String.format("消费者组 %s CPU使用率 %.2f%%，可能影响性能", 
                                consumerGroup, stats.getAvgCpuUsage()),
                        "MEDIUM",
                        Arrays.asList(
                                "优化消费逻辑",
                                "增加消费者实例",
                                "使用异步处理",
                                "检查资源分配"
                        ),
                        consumerGroup
                ));
            }

            if (stats.getAvgMemoryUsage() > 80) {
                suggestions.add(new MqOptimizationSuggestion(
                        "HIGH_CONSUMER_MEMORY_USAGE",
                        "消费者内存使用率过高",
                        String.format("消费者组 %s 内存使用率 %.2f%%，存在内存泄漏风险", 
                                consumerGroup, stats.getAvgMemoryUsage()),
                        "HIGH",
                        Arrays.asList(
                                "检查内存泄漏",
                                "优化数据结构",
                                "增加内存分配",
                                "使用对象池"
                        ),
                        consumerGroup
                ));
            }

            if (stats.getAvgThroughput() < 10) {
                suggestions.add(new MqOptimizationSuggestion(
                        "LOW_CONSUMER_THROUGHPUT",
                        "消费者吞吐量过低",
                        String.format("消费者组 %s 吞吐量仅 %.2f msg/s，建议优化", 
                                consumerGroup, stats.getAvgThroughput()),
                        "MEDIUM",
                        Arrays.asList(
                                "优化消费逻辑",
                                "使用批量消费",
                                "增加并发度",
                                "减少I/O操作"
                        ),
                        consumerGroup
                ));
            }

            if (stats.getActiveConsumers() == 0) {
                suggestions.add(new MqOptimizationSuggestion(
                        "NO_ACTIVE_CONSUMERS",
                        "没有活跃消费者",
                        String.format("消费者组 %s 没有活跃的消费者实例", consumerGroup),
                        "HIGH",
                        Arrays.asList(
                                "启动消费者实例",
                                "检查消费者状态",
                                "修复连接问题",
                                "检查配置"
                        ),
                        consumerGroup
                ));
            }
        }

        return suggestions;
    }

    /**
     * 分析死信队列
     */
    private List<MqOptimizationSuggestion> analyzeDeadLetters() {
        List<MqOptimizationSuggestion> suggestions = new ArrayList<>();

        for (Map.Entry<String, DeadLetterStats> entry : deadLetterStats.entrySet()) {
            String topicConsumer = entry.getKey();
            DeadLetterStats stats = entry.getValue();

            if (stats.getTotalDeadLetters() > 100) {
                suggestions.add(new MqOptimizationSuggestion(
                        "TOO_MANY_DEAD_LETTERS",
                        "死信消息过多",
                        String.format("队列 %s 产生 %d 条死信消息，需要检查", 
                                topicConsumer, stats.getTotalDeadLetters()),
                        "HIGH",
                        Arrays.asList(
                                "分析死信原因",
                                "修复消费逻辑",
                                "增强异常处理",
                                "手动处理死信"
                        ),
                        topicConsumer
                ));
            }

            // 分析死信原因分布
            Map<String, Long> reasonDistribution = stats.getReasonDistribution();
            for (Map.Entry<String, Long> reasonEntry : reasonDistribution.entrySet()) {
                if (reasonEntry.getValue() > 50) {
                    suggestions.add(new MqOptimizationSuggestion(
                            "FREQUENT_DEAD_LETTER_REASON",
                            "频繁的死信原因",
                            String.format("队列 %s 因 '%s' 产生 %d 条死信", 
                                    topicConsumer, reasonEntry.getKey(), reasonEntry.getValue()),
                            "MEDIUM",
                            Arrays.asList(
                                    "针对性修复此问题",
                                    "增强错误处理",
                                    "添加监控告警"
                            ),
                            topicConsumer + ":" + reasonEntry.getKey()
                    ));
                }
            }
        }

        return suggestions;
    }

    /**
     * 分析整体架构
     */
    private List<MqOptimizationSuggestion> analyzeArchitecture() {
        List<MqOptimizationSuggestion> suggestions = new ArrayList<>();

        // 分析主题数量
        Set<String> topics = sendStats.keySet().stream()
                .map(key -> key.split(":")[0])
                .collect(Collectors.toSet());
        
        if (topics.size() > 100) {
            suggestions.add(new MqOptimizationSuggestion(
                    "TOO_MANY_TOPICS",
                    "主题数量过多",
                    String.format("系统中有 %d 个主题，可能影响管理效率", topics.size()),
                    "LOW",
                    Arrays.asList(
                            "合并相似主题",
                            "使用标签分类",
                            "重新设计消息架构"
                    ),
                    null
            ));
        }

        // 分析消费者组数量
        Set<String> consumerGroups = consumeStats.keySet().stream()
                .map(key -> key.split(":")[2])
                .collect(Collectors.toSet());
        
        if (consumerGroups.size() > 50) {
            suggestions.add(new MqOptimizationSuggestion(
                    "TOO_MANY_CONSUMER_GROUPS",
                    "消费者组过多",
                    String.format("系统中有 %d 个消费者组，建议优化", consumerGroups.size()),
                    "LOW",
                    Arrays.asList(
                            "合并功能相似的消费者组",
                            "重新设计消费架构",
                            "使用共享消费模式"
                    ),
                    null
            ));
        }

        return suggestions;
    }

    /**
     * 获取积压最多的队列
     */
    public List<QueueBacklogInfo> getTopBacklogQueues(int limit) {
        return backlogStats.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().getBacklogCount(), a.getValue().getBacklogCount()))
                .limit(limit)
                .map(entry -> new QueueBacklogInfo(
                        entry.getKey(),
                        entry.getValue().getBacklogCount(),
                        entry.getValue().getOldestMessageAge()
                ))
                .collect(Collectors.toList());
    }

    /**
     * 获取死信最多的队列
     */
    public List<DeadLetterInfo> getTopDeadLetterQueues(int limit) {
        return deadLetterStats.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().getTotalDeadLetters(), a.getValue().getTotalDeadLetters()))
                .limit(limit)
                .map(entry -> new DeadLetterInfo(
                        entry.getKey(),
                        entry.getValue().getTotalDeadLetters(),
                        entry.getValue().getReasonDistribution()
                ))
                .collect(Collectors.toList());
    }

    /**
     * 计算整体评分
     */
    private int calculateOverallScore(List<MqOptimizationSuggestion> suggestions) {
        int baseScore = 100;
        
        for (MqOptimizationSuggestion suggestion : suggestions) {
            switch (suggestion.getPriority()) {
                case "HIGH":
                    baseScore -= 15;
                    break;
                case "MEDIUM":
                    baseScore -= 8;
                    break;
                case "LOW":
                    baseScore -= 3;
                    break;
            }
        }
        
        return Math.max(baseScore, 0);
    }

    /**
     * 获取统计数据
     */
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("sendStats", new HashMap<>(sendStats));
        stats.put("consumeStats", new HashMap<>(consumeStats));
        stats.put("backlogStats", new HashMap<>(backlogStats));
        stats.put("consumerStats", new HashMap<>(consumerStats));
        stats.put("deadLetterStats", new HashMap<>(deadLetterStats));
        return stats;
    }

    /**
     * 清空统计数据
     */
    public void clearStats() {
        sendStats.clear();
        consumeStats.clear();
        backlogStats.clear();
        consumerStats.clear();
        deadLetterStats.clear();
    }

    // 内部类定义
    
    /**
     * 消息发送统计
     */
    public static class MessageSendStats {
        private final AtomicLong totalMessages = new AtomicLong(0);
        private final AtomicLong totalSendTime = new AtomicLong(0);
        private final AtomicLong successCount = new AtomicLong(0);
        private final AtomicLong totalMessageSize = new AtomicLong(0);
        private volatile long maxSendTime = 0;

        public void recordSend(long sendTime, boolean success, int messageSize) {
            totalMessages.incrementAndGet();
            totalSendTime.addAndGet(sendTime);
            totalMessageSize.addAndGet(messageSize);
            
            if (success) {
                successCount.incrementAndGet();
            }
            
            synchronized (this) {
                if (sendTime > maxSendTime) {
                    maxSendTime = sendTime;
                }
            }
        }

        public double getAvgSendTime() {
            long messages = totalMessages.get();
            return messages > 0 ? (double) totalSendTime.get() / messages : 0;
        }

        public double getSuccessRate() {
            long messages = totalMessages.get();
            return messages > 0 ? (double) successCount.get() / messages * 100 : 0;
        }

        public double getAvgMessageSize() {
            long messages = totalMessages.get();
            return messages > 0 ? (double) totalMessageSize.get() / messages : 0;
        }

        // Getters
        public long getTotalMessages() { return totalMessages.get(); }
        public long getMaxSendTime() { return maxSendTime; }
    }

    /**
     * 消息消费统计
     */
    public static class MessageConsumeStats {
        private final AtomicLong totalMessages = new AtomicLong(0);
        private final AtomicLong totalConsumeTime = new AtomicLong(0);
        private final AtomicLong successCount = new AtomicLong(0);
        private final AtomicLong totalMessageAge = new AtomicLong(0);
        private volatile long maxConsumeTime = 0;

        public void recordConsume(long consumeTime, boolean success, long messageAge) {
            totalMessages.incrementAndGet();
            totalConsumeTime.addAndGet(consumeTime);
            totalMessageAge.addAndGet(messageAge);
            
            if (success) {
                successCount.incrementAndGet();
            }
            
            synchronized (this) {
                if (consumeTime > maxConsumeTime) {
                    maxConsumeTime = consumeTime;
                }
            }
        }

        public double getAvgConsumeTime() {
            long messages = totalMessages.get();
            return messages > 0 ? (double) totalConsumeTime.get() / messages : 0;
        }

        public double getSuccessRate() {
            long messages = totalMessages.get();
            return messages > 0 ? (double) successCount.get() / messages * 100 : 0;
        }

        public double getAvgMessageAge() {
            long messages = totalMessages.get();
            return messages > 0 ? (double) totalMessageAge.get() / messages : 0;
        }

        // Getters
        public long getTotalMessages() { return totalMessages.get(); }
        public long getMaxConsumeTime() { return maxConsumeTime; }
    }

    /**
     * 队列积压统计
     */
    public static class QueueBacklogStats {
        private final long backlogCount;
        private final long oldestMessageAge;
        private final long timestamp;

        public QueueBacklogStats(long backlogCount, long oldestMessageAge, long timestamp) {
            this.backlogCount = backlogCount;
            this.oldestMessageAge = oldestMessageAge;
            this.timestamp = timestamp;
        }

        // Getters
        public long getBacklogCount() { return backlogCount; }
        public long getOldestMessageAge() { return oldestMessageAge; }
        public long getTimestamp() { return timestamp; }
    }

    /**
     * 消费者性能统计
     */
    public static class ConsumerPerformanceStats {
        private final int activeConsumers;
        private final double avgThroughput;
        private final double avgCpuUsage;
        private final double avgMemoryUsage;
        private final long timestamp;

        public ConsumerPerformanceStats(int activeConsumers, double avgThroughput, 
                                      double avgCpuUsage, double avgMemoryUsage, long timestamp) {
            this.activeConsumers = activeConsumers;
            this.avgThroughput = avgThroughput;
            this.avgCpuUsage = avgCpuUsage;
            this.avgMemoryUsage = avgMemoryUsage;
            this.timestamp = timestamp;
        }

        // Getters
        public int getActiveConsumers() { return activeConsumers; }
        public double getAvgThroughput() { return avgThroughput; }
        public double getAvgCpuUsage() { return avgCpuUsage; }
        public double getAvgMemoryUsage() { return avgMemoryUsage; }
        public long getTimestamp() { return timestamp; }
    }

    /**
     * 死信统计
     */
    public static class DeadLetterStats {
        private final AtomicLong totalDeadLetters = new AtomicLong(0);
        private final Map<String, AtomicLong> reasonCount = new ConcurrentHashMap<>();

        public void recordDeadLetter(String reason) {
            totalDeadLetters.incrementAndGet();
            reasonCount.computeIfAbsent(reason, k -> new AtomicLong(0)).incrementAndGet();
        }

        public Map<String, Long> getReasonDistribution() {
            return reasonCount.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue().get()
                    ));
        }

        public long getTotalDeadLetters() { return totalDeadLetters.get(); }
    }

    // 其他内部类...
    
    public static class MqPerformanceMetrics {
        private final String name;
        private final String type;
        private final double avgTime;
        private final long maxTime;
        private final long totalCount;
        private final double successRate;
        private final double avgSize;
        private final double avgLatency;

        public MqPerformanceMetrics(String name, String type, double avgTime, long maxTime,
                                  long totalCount, double successRate, double avgSize, double avgLatency) {
            this.name = name;
            this.type = type;
            this.avgTime = avgTime;
            this.maxTime = maxTime;
            this.totalCount = totalCount;
            this.successRate = successRate;
            this.avgSize = avgSize;
            this.avgLatency = avgLatency;
        }

        // Getters
        public String getName() { return name; }
        public String getType() { return type; }
        public double getAvgTime() { return avgTime; }
        public long getMaxTime() { return maxTime; }
        public long getTotalCount() { return totalCount; }
        public double getSuccessRate() { return successRate; }
        public double getAvgSize() { return avgSize; }
        public double getAvgLatency() { return avgLatency; }
    }

    public static class MqOptimizationSuggestion {
        private final String type;
        private final String title;
        private final String description;
        private final String priority;
        private final List<String> actions;
        private final String relatedObject;

        public MqOptimizationSuggestion(String type, String title, String description,
                                      String priority, List<String> actions, String relatedObject) {
            this.type = type;
            this.title = title;
            this.description = description;
            this.priority = priority;
            this.actions = actions;
            this.relatedObject = relatedObject;
        }

        // Getters
        public String getType() { return type; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getPriority() { return priority; }
        public List<String> getActions() { return actions; }
        public String getRelatedObject() { return relatedObject; }
    }

    public static class MqOptimizationReport {
        private final Map<String, MqPerformanceMetrics> metrics;
        private final List<MqOptimizationSuggestion> suggestions;
        private final List<QueueBacklogInfo> topBacklogQueues;
        private final List<DeadLetterInfo> topDeadLetterQueues;
        private final int overallScore;
        private final long timestamp;

        public MqOptimizationReport(Map<String, MqPerformanceMetrics> metrics,
                                  List<MqOptimizationSuggestion> suggestions,
                                  List<QueueBacklogInfo> topBacklogQueues,
                                  List<DeadLetterInfo> topDeadLetterQueues,
                                  int overallScore, long timestamp) {
            this.metrics = metrics;
            this.suggestions = suggestions;
            this.topBacklogQueues = topBacklogQueues;
            this.topDeadLetterQueues = topDeadLetterQueues;
            this.overallScore = overallScore;
            this.timestamp = timestamp;
        }

        // Getters
        public Map<String, MqPerformanceMetrics> getMetrics() { return metrics; }
        public List<MqOptimizationSuggestion> getSuggestions() { return suggestions; }
        public List<QueueBacklogInfo> getTopBacklogQueues() { return topBacklogQueues; }
        public List<DeadLetterInfo> getTopDeadLetterQueues() { return topDeadLetterQueues; }
        public int getOverallScore() { return overallScore; }
        public long getTimestamp() { return timestamp; }
    }

    public static class QueueBacklogInfo {
        private final String queueName;
        private final long backlogCount;
        private final long oldestMessageAge;

        public QueueBacklogInfo(String queueName, long backlogCount, long oldestMessageAge) {
            this.queueName = queueName;
            this.backlogCount = backlogCount;
            this.oldestMessageAge = oldestMessageAge;
        }

        // Getters
        public String getQueueName() { return queueName; }
        public long getBacklogCount() { return backlogCount; }
        public long getOldestMessageAge() { return oldestMessageAge; }
    }

    public static class DeadLetterInfo {
        private final String queueName;
        private final long deadLetterCount;
        private final Map<String, Long> reasonDistribution;

        public DeadLetterInfo(String queueName, long deadLetterCount, Map<String, Long> reasonDistribution) {
            this.queueName = queueName;
            this.deadLetterCount = deadLetterCount;
            this.reasonDistribution = reasonDistribution;
        }

        // Getters
        public String getQueueName() { return queueName; }
        public long getDeadLetterCount() { return deadLetterCount; }
        public Map<String, Long> getReasonDistribution() { return reasonDistribution; }
    }
}