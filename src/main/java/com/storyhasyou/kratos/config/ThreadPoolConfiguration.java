package com.storyhasyou.kratos.config;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author fangxi
 */
@SpringBootConfiguration
public class ThreadPoolConfiguration {

    private static final String NAME_PREFIX = "kratos-pool-thread-";
    private static final int CPU = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU * 2 + 1;
    private static final int KEEP_ALIVE_TIME = 1;
    private static final int MAX_QUEUE_NUM = 1 << 7;


    @Bean
    @ConditionalOnMissingBean(ThreadPoolTaskExecutor.class)
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAXIMUM_POOL_SIZE);
        executor.setQueueCapacity(MAX_QUEUE_NUM);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix(NAME_PREFIX);
        executor.setThreadFactory(new CustomThreadFactory());
        executor.initialize();
        return executor;
    }

    @Bean
    @ConditionalOnMissingBean(ThreadPoolExecutor.class)
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(MAX_QUEUE_NUM),
                new CustomThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    private static class CustomThreadFactory implements ThreadFactory {
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        CustomThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        }

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(group, runnable, NAME_PREFIX + threadNumber.getAndIncrement(), 0);
            // 设置不是守护线程
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }

            // 设置优先级为默认的
            if (thread.getPriority() != Thread.NORM_PRIORITY) {
                thread.setPriority(Thread.NORM_PRIORITY);
            }
            return thread;
        }
    }
}
