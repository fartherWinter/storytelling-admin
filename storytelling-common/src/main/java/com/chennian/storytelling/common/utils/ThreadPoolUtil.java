package com.chennian.storytelling.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池 工具类 (采用枚举类型的单例模式)
 *
 * @author by chennian
 * @date 2023/5/20 11:45
 */
@Slf4j
public class ThreadPoolUtil {
    private enum innerEnum {
        /**
         * 单例模式
         * 枚举类中定义一个私有构造方法，并在其中创建线程池，并提供获取线程池和实例的静态方法
         */
        INSTANCE;
        private final ThreadPoolExecutor executor;
        private final ThreadPoolUtil threadPoolUtil;

        innerEnum() {
            log.info("==========线程池开启==========");
            /**
             * 创建LinkedBlockingQueue，容量Integer.MAX_VALUE 。
             */
            LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
            /**
             * 拒绝策略 ： 运行被拒绝的任务
             */
            ThreadPoolExecutor.CallerRunsPolicy policy = new ThreadPoolExecutor.CallerRunsPolicy();
            /**
             * int corePoolSize : 常驻核心线程数
             * int maximumPoolSize : 线程池能够容纳同时执行的最大线程数据
             * long keepAliveTime : 线程池中的线程空闲时间，当空闲时间达到 keepAliveTime 值时，线程会被销毁，直到只剩下 corePoolSize
             * TimeUnit unit : keepAliveTime 的时间单位
             * BlockingQueue<Runnable> workQueue : 缓存队列，当请求线程数大于 corePoolSize 时，线程进入 BlockingQueue 阻塞队列
             * RejectedExecutionHandler handler : 执行拒绝策略的对象，当活动线程数大于 maximumPoolSize 的时候，线程池通过该处理策略
             */
            executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2,
                    Runtime.getRuntime().availableProcessors() * 2,
                    60L, TimeUnit.MILLISECONDS,
                    queue,
                    policy
            );
            threadPoolUtil = new ThreadPoolUtil();
        }

        private ThreadPoolExecutor getThreadPool() {
            return executor;
        }

        private ThreadPoolUtil threadPoolUtil() {
            return threadPoolUtil;
        }
    }

    /**
     * 获取实例
     */
    public static ThreadPoolUtil getInstance() {
        return innerEnum.INSTANCE.threadPoolUtil();
    }

    /**
     * 获取线程池
     */
    public ThreadPoolExecutor getThreadPool() {
        return innerEnum.INSTANCE.getThreadPool();
    }
}
