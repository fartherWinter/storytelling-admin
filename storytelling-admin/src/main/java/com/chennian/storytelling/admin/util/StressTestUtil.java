package com.chennian.storytelling.admin.util;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 压力测试工具类
 * 提供多种压测场景和性能分析功能
 */
@Component
public class StressTestUtil {

    private static final Logger log = LoggerFactory.getLogger(StressTestUtil.class);

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private RestTemplate restTemplate;

    private final ExecutorService stressTestExecutor = Executors.newCachedThreadPool(
        r -> {
            Thread t = new Thread(r, "StressTest-" + System.currentTimeMillis());
            t.setDaemon(true);
            return t;
        }
    );

    /**
     * 压力测试结果
     */
    public static class StressTestResult {
        private final String testName;
        private final int totalRequests;
        private final int concurrency;
        private final Duration duration;
        private final long successCount;
        private final long failureCount;
        private final double avgResponseTime;
        private final double minResponseTime;
        private final double maxResponseTime;
        private final double p95ResponseTime;
        private final double p99ResponseTime;
        private final double throughput; // requests per second
        private final Map<String, Long> errorCounts;
        private final LocalDateTime startTime;
        private final LocalDateTime endTime;
        private final List<String> recommendations;

        public StressTestResult(String testName, int totalRequests, int concurrency, Duration duration,
                              long successCount, long failureCount, double avgResponseTime,
                              double minResponseTime, double maxResponseTime, double p95ResponseTime,
                              double p99ResponseTime, double throughput, Map<String, Long> errorCounts,
                              LocalDateTime startTime, LocalDateTime endTime, List<String> recommendations) {
            this.testName = testName;
            this.totalRequests = totalRequests;
            this.concurrency = concurrency;
            this.duration = duration;
            this.successCount = successCount;
            this.failureCount = failureCount;
            this.avgResponseTime = avgResponseTime;
            this.minResponseTime = minResponseTime;
            this.maxResponseTime = maxResponseTime;
            this.p95ResponseTime = p95ResponseTime;
            this.p99ResponseTime = p99ResponseTime;
            this.throughput = throughput;
            this.errorCounts = errorCounts;
            this.startTime = startTime;
            this.endTime = endTime;
            this.recommendations = recommendations;
        }

        // Getters
        public String getTestName() { return testName; }
        public int getTotalRequests() { return totalRequests; }
        public int getConcurrency() { return concurrency; }
        public Duration getDuration() { return duration; }
        public long getSuccessCount() { return successCount; }
        public long getFailureCount() { return failureCount; }
        public double getAvgResponseTime() { return avgResponseTime; }
        public double getMinResponseTime() { return minResponseTime; }
        public double getMaxResponseTime() { return maxResponseTime; }
        public double getP95ResponseTime() { return p95ResponseTime; }
        public double getP99ResponseTime() { return p99ResponseTime; }
        public double getThroughput() { return throughput; }
        public Map<String, Long> getErrorCounts() { return errorCounts; }
        public LocalDateTime getStartTime() { return startTime; }
        public LocalDateTime getEndTime() { return endTime; }
        public List<String> getRecommendations() { return recommendations; }
        public double getSuccessRate() { return totalRequests > 0 ? (double) successCount / totalRequests * 100 : 0; }
    }

    /**
     * 压力测试配置
     */
    public static class StressTestConfig {
        private String testName = "Default Stress Test";
        private String targetUrl = "http://localhost:8080";
        private int concurrency = 10;
        private int totalRequests = 1000;
        private Duration maxDuration = Duration.ofMinutes(5);
        private Duration rampUpTime = Duration.ofSeconds(10);
        private Map<String, String> headers = new HashMap<>();
        private String requestBody = "";
        private String httpMethod = "GET";
        private boolean enableMetrics = true;
        private int warmupRequests = 50;

        // Builder pattern methods
        public StressTestConfig testName(String testName) { this.testName = testName; return this; }
        public StressTestConfig targetUrl(String targetUrl) { this.targetUrl = targetUrl; return this; }
        public StressTestConfig concurrency(int concurrency) { this.concurrency = concurrency; return this; }
        public StressTestConfig totalRequests(int totalRequests) { this.totalRequests = totalRequests; return this; }
        public StressTestConfig maxDuration(Duration maxDuration) { this.maxDuration = maxDuration; return this; }
        public StressTestConfig rampUpTime(Duration rampUpTime) { this.rampUpTime = rampUpTime; return this; }
        public StressTestConfig headers(Map<String, String> headers) { this.headers = headers; return this; }
        public StressTestConfig requestBody(String requestBody) { this.requestBody = requestBody; return this; }
        public StressTestConfig httpMethod(String httpMethod) { this.httpMethod = httpMethod; return this; }
        public StressTestConfig enableMetrics(boolean enableMetrics) { this.enableMetrics = enableMetrics; return this; }
        public StressTestConfig warmupRequests(int warmupRequests) { this.warmupRequests = warmupRequests; return this; }

        // Getters
        public String getTestName() { return testName; }
        public String getTargetUrl() { return targetUrl; }
        public int getConcurrency() { return concurrency; }
        public int getTotalRequests() { return totalRequests; }
        public Duration getMaxDuration() { return maxDuration; }
        public Duration getRampUpTime() { return rampUpTime; }
        public Map<String, String> getHeaders() { return headers; }
        public String getRequestBody() { return requestBody; }
        public String getHttpMethod() { return httpMethod; }
        public boolean isEnableMetrics() { return enableMetrics; }
        public int getWarmupRequests() { return warmupRequests; }
    }

    /**
     * 执行压力测试
     */
    public CompletableFuture<StressTestResult> executeStressTest(StressTestConfig config) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("开始执行压力测试: {}", config.getTestName());
            
            LocalDateTime startTime = LocalDateTime.now();
            
            // 预热
            if (config.getWarmupRequests() > 0) {
                performWarmup(config);
            }
            
            // 执行主要测试
            StressTestResult result = performMainTest(config, startTime);
            
            log.info("压力测试完成: {}, 成功率: {:.2f}%, 平均响应时间: {:.2f}ms", 
                    config.getTestName(), result.getSuccessRate(), result.getAvgResponseTime());
            
            return result;
        }, stressTestExecutor);
    }

    /**
     * 执行预热
     */
    private void performWarmup(StressTestConfig config) {
        log.info("开始预热，请求数: {}", config.getWarmupRequests());
        
        List<CompletableFuture<Void>> warmupTasks = IntStream.range(0, config.getWarmupRequests())
            .mapToObj(i -> CompletableFuture.runAsync(() -> {
                try {
                    executeRequest(config);
                } catch (Exception e) {
                    // 预热阶段忽略错误
                }
            }, stressTestExecutor))
            .collect(Collectors.toList());
        
        CompletableFuture.allOf(warmupTasks.toArray(new CompletableFuture[0])).join();
        
        log.info("预热完成");
    }

    /**
     * 执行主要测试
     */
    private StressTestResult performMainTest(StressTestConfig config, LocalDateTime startTime) {
        final AtomicLong successCount = new AtomicLong(0);
        final AtomicLong failureCount = new AtomicLong(0);
        final List<Double> responseTimes = Collections.synchronizedList(new ArrayList<>());
        final Map<String, LongAdder> errorCounts = new ConcurrentHashMap<>();
        
        // 创建信号量控制并发
        Semaphore semaphore = new Semaphore(config.getConcurrency());
        CountDownLatch latch = new CountDownLatch(config.getTotalRequests());
        
        // 创建定时器用于记录指标
        Timer requestTimer = config.isEnableMetrics() ? 
            Timer.builder("stress.test.request.time")
                 .tag("test", config.getTestName())
                 .register(meterRegistry) : null;
        
        Counter successCounter = config.isEnableMetrics() ? 
            Counter.builder("stress.test.request.success")
                   .tag("test", config.getTestName())
                   .register(meterRegistry) : null;
        
        Counter failureCounter = config.isEnableMetrics() ? 
            Counter.builder("stress.test.request.failure")
                   .tag("test", config.getTestName())
                   .register(meterRegistry) : null;
        
        // 启动测试任务
        for (int i = 0; i < config.getTotalRequests(); i++) {
            final int requestId = i;
            
            stressTestExecutor.submit(() -> {
                try {
                    semaphore.acquire();
                    
                    // 渐进式增加负载
                    if (config.getRampUpTime().toMillis() > 0) {
                        long delay = (config.getRampUpTime().toMillis() * requestId) / config.getTotalRequests();
                        if (delay > 0) {
                            Thread.sleep(delay);
                        }
                    }
                    
                    long requestStart = System.currentTimeMillis();
                    
                    try {
                        executeRequest(config);
                        
                        long responseTime = System.currentTimeMillis() - requestStart;
                        responseTimes.add((double) responseTime);
                        successCount.incrementAndGet();
                        
                        if (requestTimer != null) {
                            requestTimer.record(responseTime, TimeUnit.MILLISECONDS);
                        }
                        if (successCounter != null) {
                            successCounter.increment();
                        }
                        
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                        
                        String errorType = e.getClass().getSimpleName();
                        errorCounts.computeIfAbsent(errorType, k -> new LongAdder()).increment();
                        
                        if (failureCounter != null) {
                            failureCounter.increment();
                        }
                        
                        log.debug("请求失败: {}", e.getMessage());
                    }
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    semaphore.release();
                    latch.countDown();
                }
            });
        }
        
        // 等待所有请求完成或超时
        try {
            boolean completed = latch.await(config.getMaxDuration().toMillis(), TimeUnit.MILLISECONDS);
            if (!completed) {
                log.warn("压力测试超时，部分请求未完成");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("压力测试被中断");
        }
        
        LocalDateTime endTime = LocalDateTime.now();
        Duration actualDuration = Duration.between(startTime, endTime);
        
        // 计算统计数据
        return calculateResults(config, startTime, endTime, actualDuration, 
                              successCount.get(), failureCount.get(), 
                              responseTimes, errorCounts);
    }

    /**
     * 执行单个请求
     */
    private void executeRequest(StressTestConfig config) {
        try {
            ResponseEntity<String> response;
            
            switch (config.getHttpMethod().toUpperCase()) {
                case "GET":
                    response = restTemplate.getForEntity(config.getTargetUrl(), String.class);
                    break;
                case "POST":
                    response = restTemplate.postForEntity(config.getTargetUrl(), config.getRequestBody(), String.class);
                    break;
                default:
                    throw new UnsupportedOperationException("不支持的HTTP方法: " + config.getHttpMethod());
            }
            
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("HTTP错误: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            throw new RuntimeException("请求执行失败: " + e.getMessage(), e);
        }
    }

    /**
     * 计算测试结果
     */
    private StressTestResult calculateResults(StressTestConfig config, LocalDateTime startTime, 
                                            LocalDateTime endTime, Duration actualDuration,
                                            long successCount, long failureCount,
                                            List<Double> responseTimes, 
                                            Map<String, LongAdder> errorCounts) {
        
        // 计算响应时间统计
        double avgResponseTime = responseTimes.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double minResponseTime = responseTimes.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
        double maxResponseTime = responseTimes.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
        
        // 计算百分位数
        List<Double> sortedTimes = responseTimes.stream().sorted().collect(Collectors.toList());
        double p95ResponseTime = calculatePercentile(sortedTimes, 95);
        double p99ResponseTime = calculatePercentile(sortedTimes, 99);
        
        // 计算吞吐量
        double throughput = actualDuration.toMillis() > 0 ? 
            (double) successCount / actualDuration.toSeconds() : 0.0;
        
        // 转换错误计数
        Map<String, Long> errorCountMap = errorCounts.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().sum()));
        
        // 生成优化建议
        List<String> recommendations = generateRecommendations(config, successCount, failureCount, 
                                                             avgResponseTime, throughput, errorCountMap);
        
        return new StressTestResult(
            config.getTestName(),
            config.getTotalRequests(),
            config.getConcurrency(),
            actualDuration,
            successCount,
            failureCount,
            avgResponseTime,
            minResponseTime,
            maxResponseTime,
            p95ResponseTime,
            p99ResponseTime,
            throughput,
            errorCountMap,
            startTime,
            endTime,
            recommendations
        );
    }

    /**
     * 计算百分位数
     */
    private double calculatePercentile(List<Double> sortedValues, int percentile) {
        if (sortedValues.isEmpty()) {
            return 0.0;
        }
        
        int index = (int) Math.ceil(percentile / 100.0 * sortedValues.size()) - 1;
        index = Math.max(0, Math.min(index, sortedValues.size() - 1));
        
        return sortedValues.get(index);
    }

    /**
     * 生成优化建议
     */
    private List<String> generateRecommendations(StressTestConfig config, long successCount, 
                                                long failureCount, double avgResponseTime, 
                                                double throughput, Map<String, Long> errorCounts) {
        List<String> recommendations = new ArrayList<>();
        
        double successRate = (double) successCount / (successCount + failureCount) * 100;
        
        // 成功率建议
        if (successRate < 95) {
            recommendations.add("成功率较低(" + String.format("%.1f", successRate) + "%)，建议检查系统稳定性和错误处理");
        }
        
        // 响应时间建议
        if (avgResponseTime > 1000) {
            recommendations.add("平均响应时间过长(" + String.format("%.1f", avgResponseTime) + "ms)，建议进行性能优化");
        } else if (avgResponseTime > 500) {
            recommendations.add("响应时间偏高(" + String.format("%.1f", avgResponseTime) + "ms)，可考虑进一步优化");
        }
        
        // 吞吐量建议
        if (throughput < 10) {
            recommendations.add("吞吐量较低(" + String.format("%.1f", throughput) + " req/s)，建议增加并发处理能力");
        }
        
        // 错误分析建议
        if (!errorCounts.isEmpty()) {
            String mostCommonError = errorCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Unknown");
            recommendations.add("主要错误类型: " + mostCommonError + "，建议针对性优化");
        }
        
        // 并发建议
        if (config.getConcurrency() < 5) {
            recommendations.add("并发数较低，可尝试增加并发数以测试系统极限");
        } else if (config.getConcurrency() > 100) {
            recommendations.add("并发数较高，注意监控系统资源使用情况");
        }
        
        return recommendations;
    }

    /**
     * 创建会议室压力测试配置
     */
    public StressTestConfig createRoomStressTest(String baseUrl, int concurrency, int totalRequests) {
        return new StressTestConfig()
            .testName("会议室创建压力测试")
            .targetUrl(baseUrl + "/api/conference/rooms")
            .httpMethod("POST")
            .concurrency(concurrency)
            .totalRequests(totalRequests)
            .requestBody("{\"roomName\":\"StressTest\",\"maxParticipants\":10}")
            .headers(Map.of("Content-Type", "application/json"));
    }

    /**
     * 创建加入房间压力测试配置
     */
    public StressTestConfig joinRoomStressTest(String baseUrl, String roomId, int concurrency, int totalRequests) {
        return new StressTestConfig()
            .testName("加入房间压力测试")
            .targetUrl(baseUrl + "/api/conference/rooms/" + roomId + "/join")
            .httpMethod("POST")
            .concurrency(concurrency)
            .totalRequests(totalRequests)
            .requestBody("{\"participantName\":\"TestUser\"}")
            .headers(Map.of("Content-Type", "application/json"));
    }

    /**
     * 创建查询房间压力测试配置
     */
    public StressTestConfig getRoomStressTest(String baseUrl, String roomId, int concurrency, int totalRequests) {
        return new StressTestConfig()
            .testName("查询房间压力测试")
            .targetUrl(baseUrl + "/api/conference/rooms/" + roomId)
            .httpMethod("GET")
            .concurrency(concurrency)
            .totalRequests(totalRequests);
    }

    /**
     * 执行综合压力测试
     */
    public CompletableFuture<Map<String, StressTestResult>> executeComprehensiveStressTest(
            String baseUrl, int concurrency, int requestsPerTest) {
        
        return CompletableFuture.supplyAsync(() -> {
            Map<String, StressTestResult> results = new ConcurrentHashMap<>();
            
            List<CompletableFuture<Void>> testTasks = Arrays.asList(
                executeStressTest(createRoomStressTest(baseUrl, concurrency, requestsPerTest))
                    .thenAccept(result -> results.put("createRoom", result)),
                    
                executeStressTest(getRoomStressTest(baseUrl, "test-room", concurrency, requestsPerTest))
                    .thenAccept(result -> results.put("getRoom", result))
            );
            
            CompletableFuture.allOf(testTasks.toArray(new CompletableFuture[0])).join();
            
            return results;
        }, stressTestExecutor);
    }

    /**
     * 获取压力测试报告
     */
    public Map<String, Object> generateStressTestReport(StressTestResult result) {
        Map<String, Object> report = new HashMap<>();
        
        report.put("testName", result.getTestName());
        report.put("summary", Map.of(
            "totalRequests", result.getTotalRequests(),
            "successCount", result.getSuccessCount(),
            "failureCount", result.getFailureCount(),
            "successRate", String.format("%.2f%%", result.getSuccessRate()),
            "duration", result.getDuration().toString(),
            "throughput", String.format("%.2f req/s", result.getThroughput())
        ));
        
        report.put("performance", Map.of(
            "avgResponseTime", String.format("%.2f ms", result.getAvgResponseTime()),
            "minResponseTime", String.format("%.2f ms", result.getMinResponseTime()),
            "maxResponseTime", String.format("%.2f ms", result.getMaxResponseTime()),
            "p95ResponseTime", String.format("%.2f ms", result.getP95ResponseTime()),
            "p99ResponseTime", String.format("%.2f ms", result.getP99ResponseTime())
        ));
        
        report.put("errors", result.getErrorCounts());
        report.put("recommendations", result.getRecommendations());
        report.put("timestamp", Map.of(
            "startTime", result.getStartTime(),
            "endTime", result.getEndTime()
        ));
        
        return report;
    }

    /**
     * 关闭压力测试执行器
     */
    public void shutdown() {
        stressTestExecutor.shutdown();
        try {
            if (!stressTestExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                stressTestExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            stressTestExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}