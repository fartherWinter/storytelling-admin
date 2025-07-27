package com.chennian.storytelling.common.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 数据库性能优化器
 * 分析数据库性能并提供优化建议
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Component
public class DatabasePerformanceOptimizer {

    @Autowired(required = false)
    private DataSource dataSource;

    // SQL执行统计
    private final Map<String, SqlExecutionStats> sqlStats = new ConcurrentHashMap<>();
    
    // 慢查询统计
    private final List<SlowQueryInfo> slowQueries = Collections.synchronizedList(new ArrayList<>());
    
    // 连接池统计
    private final Map<String, ConnectionPoolStats> poolStats = new ConcurrentHashMap<>();
    
    // 分片统计
    private final Map<String, ShardingStats> shardingStats = new ConcurrentHashMap<>();
    
    // 慢查询阈值（毫秒）
    private static final long SLOW_QUERY_THRESHOLD = 1000;
    
    // 最大慢查询记录数
    private static final int MAX_SLOW_QUERIES = 1000;

    /**
     * 记录SQL执行
     */
    public void recordSqlExecution(String sql, long executionTime, boolean success, int rowCount) {
        // 标准化SQL（移除参数值）
        String normalizedSql = normalizeSql(sql);
        
        // 更新统计
        sqlStats.computeIfAbsent(normalizedSql, k -> new SqlExecutionStats())
                .recordExecution(executionTime, success, rowCount);
        
        // 记录慢查询
        if (executionTime > SLOW_QUERY_THRESHOLD) {
            recordSlowQuery(sql, executionTime, rowCount);
        }
    }

    /**
     * 记录慢查询
     */
    private void recordSlowQuery(String sql, long executionTime, int rowCount) {
        SlowQueryInfo slowQuery = new SlowQueryInfo(
                sql, executionTime, rowCount, System.currentTimeMillis());
        
        synchronized (slowQueries) {
            slowQueries.add(slowQuery);
            // 保持最大记录数限制
            if (slowQueries.size() > MAX_SLOW_QUERIES) {
                slowQueries.remove(0);
            }
        }
    }

    /**
     * 记录连接池状态
     */
    public void recordConnectionPoolStats(String poolName, int activeConnections, 
                                        int idleConnections, int maxConnections,
                                        long avgWaitTime) {
        poolStats.put(poolName, new ConnectionPoolStats(
                activeConnections, idleConnections, maxConnections, avgWaitTime, System.currentTimeMillis()));
    }

    /**
     * 记录分片统计
     */
    public void recordShardingStats(String tableName, String shardKey, int shardIndex, 
                                  long queryTime, int resultCount) {
        shardingStats.computeIfAbsent(tableName, k -> new ShardingStats())
                    .recordQuery(shardKey, shardIndex, queryTime, resultCount);
    }

    /**
     * 分析数据库性能并生成优化报告
     */
    public DatabaseOptimizationReport analyzeAndOptimize() {
        List<DatabaseOptimizationSuggestion> suggestions = new ArrayList<>();
        Map<String, DatabasePerformanceMetrics> metricsMap = new HashMap<>();

        // 分析SQL性能
        suggestions.addAll(analyzeSqlPerformance(metricsMap));
        
        // 分析慢查询
        suggestions.addAll(analyzeSlowQueries());
        
        // 分析连接池
        suggestions.addAll(analyzeConnectionPools());
        
        // 分析分片效果
        suggestions.addAll(analyzeSharding());
        
        // 分析数据库结构
        suggestions.addAll(analyzeDatabaseStructure());

        return new DatabaseOptimizationReport(
                metricsMap,
                suggestions,
                getSlowQueries(20),
                calculateOverallScore(metricsMap, suggestions),
                System.currentTimeMillis()
        );
    }

    /**
     * 分析SQL性能
     */
    private List<DatabaseOptimizationSuggestion> analyzeSqlPerformance(
            Map<String, DatabasePerformanceMetrics> metricsMap) {
        List<DatabaseOptimizationSuggestion> suggestions = new ArrayList<>();

        for (Map.Entry<String, SqlExecutionStats> entry : sqlStats.entrySet()) {
            String sql = entry.getKey();
            SqlExecutionStats stats = entry.getValue();
            
            // 计算性能指标
            DatabasePerformanceMetrics metrics = new DatabasePerformanceMetrics(
                    sql,
                    stats.getAvgExecutionTime(),
                    stats.getMaxExecutionTime(),
                    stats.getMinExecutionTime(),
                    stats.getTotalExecutions(),
                    stats.getSuccessRate(),
                    stats.getAvgRowCount()
            );
            metricsMap.put(sql, metrics);

            // 生成优化建议
            if (stats.getAvgExecutionTime() > 500) {
                suggestions.add(new DatabaseOptimizationSuggestion(
                        "SLOW_SQL",
                        "SQL执行时间过长",
                        String.format("SQL平均执行时间 %.2fms，建议优化", stats.getAvgExecutionTime()),
                        "HIGH",
                        Arrays.asList(
                                "添加合适的索引",
                                "优化查询条件",
                                "考虑分页查询",
                                "检查表结构设计"
                        ),
                        sql
                ));
            }

            if (stats.getSuccessRate() < 95) {
                suggestions.add(new DatabaseOptimizationSuggestion(
                        "LOW_SUCCESS_RATE",
                        "SQL执行成功率过低",
                        String.format("SQL执行成功率 %.2f%%，需要检查", stats.getSuccessRate()),
                        "HIGH",
                        Arrays.asList(
                                "检查SQL语法",
                                "验证数据完整性",
                                "增加异常处理",
                                "检查权限设置"
                        ),
                        sql
                ));
            }

            if (stats.getTotalExecutions() > 10000 && stats.getAvgExecutionTime() > 100) {
                suggestions.add(new DatabaseOptimizationSuggestion(
                        "HIGH_FREQUENCY_SLOW_SQL",
                        "高频慢查询",
                        String.format("高频SQL（%d次）执行时间较长，影响系统性能", stats.getTotalExecutions()),
                        "HIGH",
                        Arrays.asList(
                                "优先优化此SQL",
                                "考虑缓存结果",
                                "数据库读写分离",
                                "使用物化视图"
                        ),
                        sql
                ));
            }
        }

        return suggestions;
    }

    /**
     * 分析慢查询
     */
    private List<DatabaseOptimizationSuggestion> analyzeSlowQueries() {
        List<DatabaseOptimizationSuggestion> suggestions = new ArrayList<>();

        if (slowQueries.size() > 100) {
            suggestions.add(new DatabaseOptimizationSuggestion(
                    "TOO_MANY_SLOW_QUERIES",
                    "慢查询过多",
                    String.format("检测到 %d 个慢查询，需要优化", slowQueries.size()),
                    "HIGH",
                    Arrays.asList(
                            "分析慢查询日志",
                            "优化索引策略",
                            "检查硬件资源",
                            "考虑分库分表"
                    ),
                    null
            ));
        }

        // 分析慢查询模式
        Map<String, Long> slowQueryPatterns = slowQueries.stream()
                .collect(Collectors.groupingBy(
                        query -> extractQueryPattern(query.getSql()),
                        Collectors.counting()
                ));

        for (Map.Entry<String, Long> entry : slowQueryPatterns.entrySet()) {
            if (entry.getValue() > 10) {
                suggestions.add(new DatabaseOptimizationSuggestion(
                        "REPEATED_SLOW_QUERY_PATTERN",
                        "重复慢查询模式",
                        String.format("查询模式 '%s' 出现 %d 次慢查询", entry.getKey(), entry.getValue()),
                        "MEDIUM",
                        Arrays.asList(
                                "针对性优化此类查询",
                                "添加专用索引",
                                "重构查询逻辑"
                        ),
                        entry.getKey()
                ));
            }
        }

        return suggestions;
    }

    /**
     * 分析连接池
     */
    private List<DatabaseOptimizationSuggestion> analyzeConnectionPools() {
        List<DatabaseOptimizationSuggestion> suggestions = new ArrayList<>();

        for (Map.Entry<String, ConnectionPoolStats> entry : poolStats.entrySet()) {
            String poolName = entry.getKey();
            ConnectionPoolStats stats = entry.getValue();

            // 连接池使用率过高
            double usageRate = (double) stats.getActiveConnections() / stats.getMaxConnections();
            if (usageRate > 0.8) {
                suggestions.add(new DatabaseOptimizationSuggestion(
                        "HIGH_CONNECTION_POOL_USAGE",
                        "连接池使用率过高",
                        String.format("连接池 %s 使用率 %.2f%%，可能影响性能", poolName, usageRate * 100),
                        "MEDIUM",
                        Arrays.asList(
                                "增加最大连接数",
                                "优化连接使用",
                                "检查连接泄漏",
                                "使用连接池监控"
                        ),
                        poolName
                ));
            }

            // 等待时间过长
            if (stats.getAvgWaitTime() > 1000) {
                suggestions.add(new DatabaseOptimizationSuggestion(
                        "HIGH_CONNECTION_WAIT_TIME",
                        "连接等待时间过长",
                        String.format("连接池 %s 平均等待时间 %dms", poolName, stats.getAvgWaitTime()),
                        "HIGH",
                        Arrays.asList(
                                "增加连接池大小",
                                "优化数据库性能",
                                "检查长事务",
                                "使用读写分离"
                        ),
                        poolName
                ));
            }
        }

        return suggestions;
    }

    /**
     * 分析分片效果
     */
    private List<DatabaseOptimizationSuggestion> analyzeSharding() {
        List<DatabaseOptimizationSuggestion> suggestions = new ArrayList<>();

        for (Map.Entry<String, ShardingStats> entry : shardingStats.entrySet()) {
            String tableName = entry.getKey();
            ShardingStats stats = entry.getValue();

            // 分析分片均衡性
            Map<Integer, Long> shardDistribution = stats.getShardDistribution();
            if (!shardDistribution.isEmpty()) {
                long maxQueries = Collections.max(shardDistribution.values());
                long minQueries = Collections.min(shardDistribution.values());
                
                if (maxQueries > minQueries * 3) {
                    suggestions.add(new DatabaseOptimizationSuggestion(
                            "UNBALANCED_SHARDING",
                            "分片不均衡",
                            String.format("表 %s 的分片查询分布不均衡，最大/最小比例为 %.2f", 
                                    tableName, (double) maxQueries / minQueries),
                            "MEDIUM",
                            Arrays.asList(
                                    "重新评估分片键",
                                    "调整分片算法",
                                    "考虑重新分片",
                                    "使用一致性哈希"
                            ),
                            tableName
                    ));
                }
            }

            // 分析跨分片查询
            if (stats.getCrossShardQueries() > stats.getTotalQueries() * 0.3) {
                suggestions.add(new DatabaseOptimizationSuggestion(
                        "TOO_MANY_CROSS_SHARD_QUERIES",
                        "跨分片查询过多",
                        String.format("表 %s 的跨分片查询占比 %.2f%%", 
                                tableName, (double) stats.getCrossShardQueries() / stats.getTotalQueries() * 100),
                        "HIGH",
                        Arrays.asList(
                                "优化查询逻辑",
                                "重新设计分片键",
                                "使用数据冗余",
                                "考虑垂直分片"
                        ),
                        tableName
                ));
            }
        }

        return suggestions;
    }

    /**
     * 分析数据库结构
     */
    private List<DatabaseOptimizationSuggestion> analyzeDatabaseStructure() {
        List<DatabaseOptimizationSuggestion> suggestions = new ArrayList<>();

        if (dataSource != null) {
            try (Connection conn = dataSource.getConnection()) {
                DatabaseMetaData metaData = conn.getMetaData();
                
                // 分析表结构
                suggestions.addAll(analyzeTableStructure(metaData));
                
                // 分析索引
                suggestions.addAll(analyzeIndexes(metaData));
                
            } catch (SQLException e) {
                // 记录错误但不影响其他分析
                System.err.println("Failed to analyze database structure: " + e.getMessage());
            }
        }

        return suggestions;
    }

    /**
     * 分析表结构
     */
    private List<DatabaseOptimizationSuggestion> analyzeTableStructure(DatabaseMetaData metaData) 
            throws SQLException {
        List<DatabaseOptimizationSuggestion> suggestions = new ArrayList<>();

        ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});
        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");
            
            // 检查表的列数
            ResultSet columns = metaData.getColumns(null, null, tableName, null);
            int columnCount = 0;
            while (columns.next()) {
                columnCount++;
            }
            columns.close();
            
            if (columnCount > 50) {
                suggestions.add(new DatabaseOptimizationSuggestion(
                        "TOO_MANY_COLUMNS",
                        "表列数过多",
                        String.format("表 %s 有 %d 列，建议考虑垂直分割", tableName, columnCount),
                        "LOW",
                        Arrays.asList(
                                "垂直分割表",
                                "使用JSON字段",
                                "重新设计表结构"
                        ),
                        tableName
                ));
            }
        }
        tables.close();

        return suggestions;
    }

    /**
     * 分析索引
     */
    private List<DatabaseOptimizationSuggestion> analyzeIndexes(DatabaseMetaData metaData) 
            throws SQLException {
        List<DatabaseOptimizationSuggestion> suggestions = new ArrayList<>();

        // 这里可以添加索引分析逻辑
        // 由于不同数据库的索引信息获取方式不同，这里提供基本框架
        
        return suggestions;
    }

    /**
     * 标准化SQL
     */
    private String normalizeSql(String sql) {
        return sql.replaceAll("\\b\\d+\\b", "?")  // 替换数字
                  .replaceAll("'[^']*'", "?")      // 替换字符串
                  .replaceAll("\\s+", " ")         // 标准化空格
                  .trim();
    }

    /**
     * 提取查询模式
     */
    private String extractQueryPattern(String sql) {
        String normalized = normalizeSql(sql);
        // 提取主要的查询模式（SELECT/INSERT/UPDATE/DELETE + 主表名）
        if (normalized.toUpperCase().startsWith("SELECT")) {
            return "SELECT_PATTERN";
        } else if (normalized.toUpperCase().startsWith("INSERT")) {
            return "INSERT_PATTERN";
        } else if (normalized.toUpperCase().startsWith("UPDATE")) {
            return "UPDATE_PATTERN";
        } else if (normalized.toUpperCase().startsWith("DELETE")) {
            return "DELETE_PATTERN";
        }
        return "OTHER_PATTERN";
    }

    /**
     * 计算整体评分
     */
    private int calculateOverallScore(Map<String, DatabasePerformanceMetrics> metricsMap,
                                    List<DatabaseOptimizationSuggestion> suggestions) {
        int baseScore = 100;
        
        // 根据建议数量和优先级扣分
        for (DatabaseOptimizationSuggestion suggestion : suggestions) {
            switch (suggestion.getPriority()) {
                case "HIGH":
                    baseScore -= 10;
                    break;
                case "MEDIUM":
                    baseScore -= 5;
                    break;
                case "LOW":
                    baseScore -= 2;
                    break;
            }
        }
        
        // 根据慢查询数量扣分
        baseScore -= Math.min(slowQueries.size() / 10, 20);
        
        return Math.max(baseScore, 0);
    }

    /**
     * 获取慢查询列表
     */
    public List<SlowQueryInfo> getSlowQueries(int limit) {
        synchronized (slowQueries) {
            return slowQueries.stream()
                    .sorted((a, b) -> Long.compare(b.getExecutionTime(), a.getExecutionTime()))
                    .limit(limit)
                    .collect(Collectors.toList());
        }
    }

    /**
     * 获取SQL统计
     */
    public Map<String, SqlExecutionStats> getSqlStats() {
        return new HashMap<>(sqlStats);
    }

    /**
     * 清空统计数据
     */
    public void clearStats() {
        sqlStats.clear();
        slowQueries.clear();
        poolStats.clear();
        shardingStats.clear();
    }

    // 内部类定义
    
    /**
     * SQL执行统计
     */
    public static class SqlExecutionStats {
        private final AtomicLong totalExecutions = new AtomicLong(0);
        private final AtomicLong totalExecutionTime = new AtomicLong(0);
        private final AtomicLong successCount = new AtomicLong(0);
        private final AtomicLong totalRowCount = new AtomicLong(0);
        private volatile long maxExecutionTime = 0;
        private volatile long minExecutionTime = Long.MAX_VALUE;

        public void recordExecution(long executionTime, boolean success, int rowCount) {
            totalExecutions.incrementAndGet();
            totalExecutionTime.addAndGet(executionTime);
            totalRowCount.addAndGet(rowCount);
            
            if (success) {
                successCount.incrementAndGet();
            }
            
            // 更新最大最小执行时间
            synchronized (this) {
                if (executionTime > maxExecutionTime) {
                    maxExecutionTime = executionTime;
                }
                if (executionTime < minExecutionTime) {
                    minExecutionTime = executionTime;
                }
            }
        }

        public double getAvgExecutionTime() {
            long executions = totalExecutions.get();
            return executions > 0 ? (double) totalExecutionTime.get() / executions : 0;
        }

        public double getSuccessRate() {
            long executions = totalExecutions.get();
            return executions > 0 ? (double) successCount.get() / executions * 100 : 0;
        }

        public double getAvgRowCount() {
            long executions = totalExecutions.get();
            return executions > 0 ? (double) totalRowCount.get() / executions : 0;
        }

        // Getters
        public long getTotalExecutions() { return totalExecutions.get(); }
        public long getTotalExecutionTime() { return totalExecutionTime.get(); }
        public long getSuccessCount() { return successCount.get(); }
        public long getMaxExecutionTime() { return maxExecutionTime; }
        public long getMinExecutionTime() { return minExecutionTime == Long.MAX_VALUE ? 0 : minExecutionTime; }
    }

    /**
     * 慢查询信息
     */
    public static class SlowQueryInfo {
        private final String sql;
        private final long executionTime;
        private final int rowCount;
        private final long timestamp;

        public SlowQueryInfo(String sql, long executionTime, int rowCount, long timestamp) {
            this.sql = sql;
            this.executionTime = executionTime;
            this.rowCount = rowCount;
            this.timestamp = timestamp;
        }

        // Getters
        public String getSql() { return sql; }
        public long getExecutionTime() { return executionTime; }
        public int getRowCount() { return rowCount; }
        public long getTimestamp() { return timestamp; }
    }

    /**
     * 连接池统计
     */
    public static class ConnectionPoolStats {
        private final int activeConnections;
        private final int idleConnections;
        private final int maxConnections;
        private final long avgWaitTime;
        private final long timestamp;

        public ConnectionPoolStats(int activeConnections, int idleConnections, 
                                 int maxConnections, long avgWaitTime, long timestamp) {
            this.activeConnections = activeConnections;
            this.idleConnections = idleConnections;
            this.maxConnections = maxConnections;
            this.avgWaitTime = avgWaitTime;
            this.timestamp = timestamp;
        }

        // Getters
        public int getActiveConnections() { return activeConnections; }
        public int getIdleConnections() { return idleConnections; }
        public int getMaxConnections() { return maxConnections; }
        public long getAvgWaitTime() { return avgWaitTime; }
        public long getTimestamp() { return timestamp; }
    }

    /**
     * 分片统计
     */
    public static class ShardingStats {
        private final Map<Integer, AtomicLong> shardQueryCount = new ConcurrentHashMap<>();
        private final AtomicLong totalQueries = new AtomicLong(0);
        private final AtomicLong crossShardQueries = new AtomicLong(0);
        private final AtomicLong totalQueryTime = new AtomicLong(0);

        public void recordQuery(String shardKey, int shardIndex, long queryTime, int resultCount) {
            totalQueries.incrementAndGet();
            totalQueryTime.addAndGet(queryTime);
            shardQueryCount.computeIfAbsent(shardIndex, k -> new AtomicLong(0)).incrementAndGet();
            
            // 简单判断是否为跨分片查询（实际实现可能更复杂）
            if (shardKey == null || shardKey.contains(",")) {
                crossShardQueries.incrementAndGet();
            }
        }

        public Map<Integer, Long> getShardDistribution() {
            return shardQueryCount.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue().get()
                    ));
        }

        // Getters
        public long getTotalQueries() { return totalQueries.get(); }
        public long getCrossShardQueries() { return crossShardQueries.get(); }
        public long getTotalQueryTime() { return totalQueryTime.get(); }
    }

    /**
     * 数据库性能指标
     */
    public static class DatabasePerformanceMetrics {
        private final String sql;
        private final double avgExecutionTime;
        private final long maxExecutionTime;
        private final long minExecutionTime;
        private final long totalExecutions;
        private final double successRate;
        private final double avgRowCount;

        public DatabasePerformanceMetrics(String sql, double avgExecutionTime, long maxExecutionTime,
                                        long minExecutionTime, long totalExecutions, 
                                        double successRate, double avgRowCount) {
            this.sql = sql;
            this.avgExecutionTime = avgExecutionTime;
            this.maxExecutionTime = maxExecutionTime;
            this.minExecutionTime = minExecutionTime;
            this.totalExecutions = totalExecutions;
            this.successRate = successRate;
            this.avgRowCount = avgRowCount;
        }

        // Getters
        public String getSql() { return sql; }
        public double getAvgExecutionTime() { return avgExecutionTime; }
        public long getMaxExecutionTime() { return maxExecutionTime; }
        public long getMinExecutionTime() { return minExecutionTime; }
        public long getTotalExecutions() { return totalExecutions; }
        public double getSuccessRate() { return successRate; }
        public double getAvgRowCount() { return avgRowCount; }
    }

    /**
     * 数据库优化建议
     */
    public static class DatabaseOptimizationSuggestion {
        private final String type;
        private final String title;
        private final String description;
        private final String priority;
        private final List<String> actions;
        private final String relatedObject;

        public DatabaseOptimizationSuggestion(String type, String title, String description,
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

    /**
     * 数据库优化报告
     */
    public static class DatabaseOptimizationReport {
        private final Map<String, DatabasePerformanceMetrics> metrics;
        private final List<DatabaseOptimizationSuggestion> suggestions;
        private final List<SlowQueryInfo> slowQueries;
        private final int overallScore;
        private final long timestamp;

        public DatabaseOptimizationReport(Map<String, DatabasePerformanceMetrics> metrics,
                                        List<DatabaseOptimizationSuggestion> suggestions,
                                        List<SlowQueryInfo> slowQueries,
                                        int overallScore, long timestamp) {
            this.metrics = metrics;
            this.suggestions = suggestions;
            this.slowQueries = slowQueries;
            this.overallScore = overallScore;
            this.timestamp = timestamp;
        }

        // Getters
        public Map<String, DatabasePerformanceMetrics> getMetrics() { return metrics; }
        public List<DatabaseOptimizationSuggestion> getSuggestions() { return suggestions; }
        public List<SlowQueryInfo> getSlowQueries() { return slowQueries; }
        public int getOverallScore() { return overallScore; }
        public long getTimestamp() { return timestamp; }
    }
}