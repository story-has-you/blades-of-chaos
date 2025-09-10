package com.storyhasyou.kratos.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author fangxi
 */
@SpringBootConfiguration
public class ThreadPoolConfiguration {

    private static final String NAME_PREFIX = "storyhasyou-pool-thread-";
    /**
     * 获取运行时机器CPU个数
     */
    private static final int CPU = Runtime.getRuntime().availableProcessors();
    /**
     * 核心线程数
     */
    private static final int CORE_POOL_SIZE = CPU + 1;
    /**
     * 最大线程数
     */
    private static final int MAXIMUM_POOL_SIZE = CPU * 2 + 1;
    /**
     * 等待时间及等待单位，这里是60秒
     */
    private static final int KEEP_ALIVE_TIME = 60;
    /**
     * 任务队列大小及队列类型，这里是 LinkedBlockingQueue
     */
    private static final int MAX_QUEUE_NUM = 1 << 7;
    /**
     * 拒绝策略，当任务已满，不在新线程中执行任务，而是有调用者所在的线程来执行
     */
    private static final RejectedExecutionHandler REJECTED_EXECUTION_HANDLER = new ThreadPoolExecutor.CallerRunsPolicy();
    /**
     * 自定义线程工厂
     */
    private static final ThreadFactory CUSTOM_THREAD_FACTORY = new CustomizableThreadFactory(NAME_PREFIX);


    /**
     * 构建Spring封装的线程池 {@link ThreadPoolTaskExecutor}
     */
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAXIMUM_POOL_SIZE);
        executor.setQueueCapacity(MAX_QUEUE_NUM);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(REJECTED_EXECUTION_HANDLER);
        executor.setThreadNamePrefix(NAME_PREFIX);
        executor.setThreadFactory(CUSTOM_THREAD_FACTORY);
        return executor;
    }

    /**
     * 构建JDK自带的线程池 {@link ThreadPoolExecutor}
     */
    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        return threadPoolTaskExecutor.getThreadPoolExecutor();
    }
}
