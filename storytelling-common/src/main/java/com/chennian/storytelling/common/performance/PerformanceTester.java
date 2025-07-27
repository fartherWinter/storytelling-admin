package com.chennian.storytelling.common.performance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

/**
 * 性能测试工具
 * 提供压力测试、并发测试、基准测试等功能
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Component
public class PerformanceTester {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceTester.class);

    @Autowired
    private PerformanceMonitor performanceMonitor;

    /**
     * 执行压力测试
     * 
     * @param testName 测试名称
     * @param task 测试任务
     * @param config 测试配置
     * @return 测试结果
     */
    public StressTestResult executeStressTest(String testName, Runnable task, StressTestConfig config) {
        logger.info("Starting stress test: {}", testName);
        
        ExecutorService executor = Executors.newFixedThreadPool(config.getConcurrentUsers());
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(config.getConcurrentUsers());
        
        AtomicLong totalExecutions = new AtomicLong(0);
        AtomicLong totalErrors = new AtomicLong(0);
        AtomicLong totalResponseTime = new AtomicLong(0);
        AtomicLong minResponseTime = new AtomicLong(Long.MAX_VALUE);
        AtomicLong maxResponseTime = new AtomicLong(0);
        
        List<Future<?>> futures = new ArrayList<>();
        
        // 启动并发用户
        for (int i = 0; i < config.getConcurrentUsers(); i++) {
            Future<?> future = executor.submit(() -> {
                try {
                    startLatch.await(); // 等待统一开始
                    
                    long userStartTime = System.currentTimeMillis();
                    long userEndTime = userStartTime + config.getDurationSeconds() * 1000;
                    
                    while (System.currentTimeMillis() < userEndTime) {
                        long taskStartTime = System.nanoTime();
                        try {
                            task.run();
                            totalExecutions.incrementAndGet();
                        } catch (Exception e) {
                            totalErrors.incrementAndGet();
                            logger.debug("Task execution error", e);
                        }
                        
                        long taskEndTime = System.nanoTime();
                        long responseTime = (taskEndTime - taskStartTime) / 1_000_000; // 转换为毫秒
                        
                        totalResponseTime.addAndGet(responseTime);
                        updateMinMax(minResponseTime, maxResponseTime, responseTime);
                        
                        // 控制请求频率
                        if (config.getRequestsPerSecond() > 0) {
                            try {
                                Thread.sleep(1000 / config.getRequestsPerSecond());
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
            futures.add(future);
        }
        
        long testStartTime = System.currentTimeMillis();
        startLatch.countDown(); // 开始测试
        
        try {
            endLatch.await(); // 等待所有用户完成
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        long testEndTime = System.currentTimeMillis();
        long actualDuration = testEndTime - testStartTime;
        
        executor.shutdown();
        
        // 计算结果
        long executions = totalExecutions.get();
        long errors = totalErrors.get();
        long avgResponseTime = executions > 0 ? totalResponseTime.get() / executions : 0;
        double throughput = executions > 0 ? (double) executions / (actualDuration / 1000.0) : 0;
        double errorRate = executions > 0 ? (double) errors / executions : 0;
        
        StressTestResult result = new StressTestResult(
                testName,
                config.getConcurrentUsers(),
                actualDuration,
                executions,
                errors,
                throughput,
                errorRate,
                avgResponseTime,
                minResponseTime.get() == Long.MAX_VALUE ? 0 : minResponseTime.get(),
                maxResponseTime.get()
        );
        
        logger.info("Stress test completed: {}", result);
        return result;
    }

    /**
     * 执行基准测试
     * 
     * @param testName 测试名称
     * @param task 测试任务
     * @param iterations 迭代次数
     * @return 基准测试结果
     */
    public BenchmarkResult executeBenchmark(String testName, Runnable task, int iterations) {
        logger.info("Starting benchmark test: {} with {} iterations", testName, iterations);
        
        List<Long> executionTimes = new ArrayList<>();
        long totalTime = 0;
        int errors = 0;
        
        // 预热
        for (int i = 0; i < Math.min(iterations / 10, 100); i++) {
            try {
                task.run();
            } catch (Exception e) {
                // 忽略预热阶段的错误
            }
        }
        
        // 正式测试
        for (int i = 0; i < iterations; i++) {
            long startTime = System.nanoTime();
            try {
                task.run();
            } catch (Exception e) {
                errors++;
                logger.debug("Benchmark task execution error", e);
            }
            long endTime = System.nanoTime();
            
            long executionTime = (endTime - startTime) / 1_000_000; // 转换为毫秒
            executionTimes.add(executionTime);
            totalTime += executionTime;
        }
        
        // 计算统计信息
        executionTimes.sort(Long::compareTo);
        long minTime = executionTimes.get(0);
        long maxTime = executionTimes.get(executionTimes.size() - 1);
        long avgTime = totalTime / iterations;
        long medianTime = executionTimes.get(iterations / 2);
        long p95Time = executionTimes.get((int) (iterations * 0.95));
        long p99Time = executionTimes.get((int) (iterations * 0.99));
        
        BenchmarkResult result = new BenchmarkResult(
                testName,
                iterations,
                errors,
                totalTime,
                avgTime,
                minTime,
                maxTime,
                medianTime,
                p95Time,
                p99Time
        );
        
        logger.info("Benchmark test completed: {}", result);
        return result;
    }

    /**
     * 执行并发测试
     * 
     * @param testName 测试名称
     * @param taskSupplier 任务供应商
     * @param concurrency 并发数
     * @param tasksPerThread 每个线程的任务数
     * @return 并发测试结果
     */
    public <T> ConcurrencyTestResult<T> executeConcurrencyTest(String testName, 
                                                              Supplier<T> taskSupplier,
                                                              int concurrency, 
                                                              int tasksPerThread) {
        logger.info("Starting concurrency test: {} with {} threads, {} tasks per thread", 
                   testName, concurrency, tasksPerThread);
        
        ExecutorService executor = Executors.newFixedThreadPool(concurrency);
        List<Future<List<T>>> futures = new ArrayList<>();
        
        long startTime = System.currentTimeMillis();
        
        // 提交任务
        for (int i = 0; i < concurrency; i++) {
            Future<List<T>> future = executor.submit(() -> {
                List<T> results = new ArrayList<>();
                for (int j = 0; j < tasksPerThread; j++) {
                    try {
                        T result = taskSupplier.get();
                        results.add(result);
                    } catch (Exception e) {
                        logger.debug("Concurrency task execution error", e);
                    }
                }
                return results;
            });
            futures.add(future);
        }
        
        // 收集结果
        List<T> allResults = new ArrayList<>();
        int errors = 0;
        
        for (Future<List<T>> future : futures) {
            try {
                List<T> results = future.get();
                allResults.addAll(results);
            } catch (Exception e) {
                errors++;
                logger.error("Error getting concurrency test result", e);
            }
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        executor.shutdown();
        
        ConcurrencyTestResult<T> result = new ConcurrencyTestResult<>(
                testName,
                concurrency,
                tasksPerThread,
                duration,
                allResults,
                errors
        );
        
        logger.info("Concurrency test completed: {}", result);
        return result;
    }

    /**
     * 更新最小最大值
     */
    private void updateMinMax(AtomicLong min, AtomicLong max, long value) {
        min.updateAndGet(current -> Math.min(current, value));
        max.updateAndGet(current -> Math.max(current, value));
    }

    /**
     * 压力测试配置
     */
    public static class StressTestConfig {
        private final int concurrentUsers;
        private final int durationSeconds;
        private final int requestsPerSecond;

        public StressTestConfig(int concurrentUsers, int durationSeconds, int requestsPerSecond) {
            this.concurrentUsers = concurrentUsers;
            this.durationSeconds = durationSeconds;
            this.requestsPerSecond = requestsPerSecond;
        }

        public int getConcurrentUsers() { return concurrentUsers; }
        public int getDurationSeconds() { return durationSeconds; }
        public int getRequestsPerSecond() { return requestsPerSecond; }
    }

    /**
     * 压力测试结果
     */
    public static class StressTestResult {
        private final String testName;
        private final int concurrentUsers;
        private final long durationMs;
        private final long totalExecutions;
        private final long totalErrors;
        private final double throughput;
        private final double errorRate;
        private final long avgResponseTime;
        private final long minResponseTime;
        private final long maxResponseTime;

        public StressTestResult(String testName, int concurrentUsers, long durationMs,
                               long totalExecutions, long totalErrors, double throughput,
                               double errorRate, long avgResponseTime, long minResponseTime,
                               long maxResponseTime) {
            this.testName = testName;
            this.concurrentUsers = concurrentUsers;
            this.durationMs = durationMs;
            this.totalExecutions = totalExecutions;
            this.totalErrors = totalErrors;
            this.throughput = throughput;
            this.errorRate = errorRate;
            this.avgResponseTime = avgResponseTime;
            this.minResponseTime = minResponseTime;
            this.maxResponseTime = maxResponseTime;
        }

        // Getters
        public String getTestName() { return testName; }
        public int getConcurrentUsers() { return concurrentUsers; }
        public long getDurationMs() { return durationMs; }
        public long getTotalExecutions() { return totalExecutions; }
        public long getTotalErrors() { return totalErrors; }
        public double getThroughput() { return throughput; }
        public double getErrorRate() { return errorRate; }
        public long getAvgResponseTime() { return avgResponseTime; }
        public long getMinResponseTime() { return minResponseTime; }
        public long getMaxResponseTime() { return maxResponseTime; }

        @Override
        public String toString() {
            return String.format("StressTestResult{name='%s', users=%d, duration=%dms, executions=%d, " +
                               "errors=%d, throughput=%.2f/s, errorRate=%.2f%%, avgTime=%dms, minTime=%dms, maxTime=%dms}",
                               testName, concurrentUsers, durationMs, totalExecutions, totalErrors,
                               throughput, errorRate * 100, avgResponseTime, minResponseTime, maxResponseTime);
        }
    }

    /**
     * 基准测试结果
     */
    public static class BenchmarkResult {
        private final String testName;
        private final int iterations;
        private final int errors;
        private final long totalTime;
        private final long avgTime;
        private final long minTime;
        private final long maxTime;
        private final long medianTime;
        private final long p95Time;
        private final long p99Time;

        public BenchmarkResult(String testName, int iterations, int errors, long totalTime,
                              long avgTime, long minTime, long maxTime, long medianTime,
                              long p95Time, long p99Time) {
            this.testName = testName;
            this.iterations = iterations;
            this.errors = errors;
            this.totalTime = totalTime;
            this.avgTime = avgTime;
            this.minTime = minTime;
            this.maxTime = maxTime;
            this.medianTime = medianTime;
            this.p95Time = p95Time;
            this.p99Time = p99Time;
        }

        // Getters
        public String getTestName() { return testName; }
        public int getIterations() { return iterations; }
        public int getErrors() { return errors; }
        public long getTotalTime() { return totalTime; }
        public long getAvgTime() { return avgTime; }
        public long getMinTime() { return minTime; }
        public long getMaxTime() { return maxTime; }
        public long getMedianTime() { return medianTime; }
        public long getP95Time() { return p95Time; }
        public long getP99Time() { return p99Time; }

        @Override
        public String toString() {
            return String.format("BenchmarkResult{name='%s', iterations=%d, errors=%d, totalTime=%dms, " +
                               "avgTime=%dms, minTime=%dms, maxTime=%dms, medianTime=%dms, p95=%dms, p99=%dms}",
                               testName, iterations, errors, totalTime, avgTime, minTime, maxTime,
                               medianTime, p95Time, p99Time);
        }
    }

    /**
     * 并发测试结果
     */
    public static class ConcurrencyTestResult<T> {
        private final String testName;
        private final int concurrency;
        private final int tasksPerThread;
        private final long durationMs;
        private final List<T> results;
        private final int errors;

        public ConcurrencyTestResult(String testName, int concurrency, int tasksPerThread,
                                   long durationMs, List<T> results, int errors) {
            this.testName = testName;
            this.concurrency = concurrency;
            this.tasksPerThread = tasksPerThread;
            this.durationMs = durationMs;
            this.results = results;
            this.errors = errors;
        }

        // Getters
        public String getTestName() { return testName; }
        public int getConcurrency() { return concurrency; }
        public int getTasksPerThread() { return tasksPerThread; }
        public long getDurationMs() { return durationMs; }
        public List<T> getResults() { return results; }
        public int getErrors() { return errors; }
        public int getTotalTasks() { return concurrency * tasksPerThread; }
        public int getSuccessfulTasks() { return results.size(); }
        public double getSuccessRate() {
            int total = getTotalTasks();
            return total > 0 ? (double) getSuccessfulTasks() / total : 0;
        }

        @Override
        public String toString() {
            return String.format("ConcurrencyTestResult{name='%s', concurrency=%d, tasksPerThread=%d, " +
                               "duration=%dms, successful=%d, errors=%d, successRate=%.2f%%}",
                               testName, concurrency, tasksPerThread, durationMs, getSuccessfulTasks(),
                               errors, getSuccessRate() * 100);
        }
    }
}