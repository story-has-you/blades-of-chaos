package com.storyhasyou.kratos.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置类
 * 
 * <p>提供两种类型的执行器：</p>
 * <ul>
 *     <li><strong>传统线程池</strong>：适用于CPU密集型任务，有限的线程数量和队列管理</li>
 *     <li><strong>Virtual Thread执行器</strong>：适用于I/O密集型任务，JDK 21新特性，轻量级并发</li>
 * </ul>
 * 
 * <p><strong>Virtual Thread特性（JDK 21+）：</strong></p>
 * <ul>
 *     <li>轻量级：每个Virtual Thread占用内存极少（约2KB vs 传统线程1-2MB）</li>
 *     <li>高并发：可轻松创建数百万个Virtual Thread</li>
 *     <li>适合I/O密集型：自动在I/O阻塞时让出CPU</li>
 *     <li>简化编程模型：使用传统同步代码即可获得异步性能</li>
 * </ul>
 * 
 * <p><strong>使用建议：</strong></p>
 * <ul>
 *     <li>CPU密集型任务：使用 {@code threadPoolTaskExecutor} 或 {@code threadPoolExecutor}</li>
 *     <li>I/O密集型任务：使用 {@code virtualThreadExecutor}</li>
 *     <li>高并发网络请求：优先考虑 {@code virtualThreadExecutor}</li>
 *     <li>数据库操作、文件I/O：推荐使用 {@code virtualThreadExecutor}</li>
 * </ul>
 *
 * @author fangxi
 * @since JDK 21
 */
@SpringBootConfiguration
public class ThreadPoolConfiguration {

    private static final String NAME_PREFIX = "storyhasyou-pool-thread-";
    
    private static final String VIRTUAL_THREAD_NAME_PREFIX = "storyhasyou-virtual-thread-";
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
     * 构建Spring封装的传统线程池 {@link ThreadPoolTaskExecutor}
     * 
     * <p><strong>适用场景：</strong></p>
     * <ul>
     *     <li>CPU密集型任务（计算、数据处理、算法执行）</li>
     *     <li>需要线程池管理和监控的场景</li>
     *     <li>对线程数量有严格控制要求的任务</li>
     * </ul>
     * 
     * @return 传统线程池执行器实例
     */
    @Bean("threadPoolTaskExecutor")
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
     * 构建JDK自带的传统线程池 {@link ThreadPoolExecutor}
     * 
     * <p>基于 {@link ThreadPoolTaskExecutor} 获取底层的 {@link ThreadPoolExecutor} 实例</p>
     * 
     * @param threadPoolTaskExecutor Spring封装的线程池实例
     * @return JDK原生线程池执行器实例
     */
    @Bean("threadPoolExecutor")
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        return threadPoolTaskExecutor.getThreadPoolExecutor();
    }

    /**
     * 构建JDK 21的Virtual Thread执行器 {@link Executor}
     * 
     * <p><strong>Virtual Thread特性：</strong></p>
     * <ul>
     *     <li><strong>轻量级</strong>：每个Virtual Thread仅占用约2KB内存，而传统线程需要1-2MB</li>
     *     <li><strong>高并发</strong>：可同时运行数百万个Virtual Thread而不会耗尽内存</li>
     *     <li><strong>自动调度</strong>：JVM自动将Virtual Thread映射到操作系统线程上</li>
     *     <li><strong>I/O友好</strong>：在I/O阻塞时自动让出CPU，无需手动异步编程</li>
     *     <li><strong>向后兼容</strong>：完全兼容现有的Thread API</li>
     * </ul>
     * 
     * <p><strong>适用场景：</strong></p>
     * <ul>
     *     <li><strong>I/O密集型任务</strong>：网络请求、数据库操作、文件读写</li>
     *     <li><strong>高并发场景</strong>：大量并发HTTP请求处理</li>
     *     <li><strong>阻塞式API调用</strong>：第三方服务调用、消息队列消费</li>
     *     <li><strong>微服务间通信</strong>：服务网格中的大量并发调用</li>
     * </ul>
     * 
     * <p><strong>注意事项：</strong></p>
     * <ul>
     *     <li>不适合CPU密集型任务（建议使用传统线程池）</li>
     *     <li>避免使用ThreadLocal（可能导致内存泄漏）</li>
     *     <li>谨慎使用同步代码块（可能阻塞carrier thread）</li>
     * </ul>
     * 
     * <p><strong>使用示例：</strong></p>
     * <pre>{@code
     * @Autowired
     * @Qualifier("virtualThreadExecutor")
     * private Executor virtualThreadExecutor;
     * 
     * // 异步执行I/O密集型任务
     * virtualThreadExecutor.execute(() -> {
     *     // 执行网络请求、数据库查询等I/O操作
     *     performDatabaseQuery();
     * });
     * }</pre>
     * 
     * @return Virtual Thread执行器实例
     * @since JDK 21
     */
    @Bean("virtualThreadExecutor")
    public Executor virtualThreadExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    /**
     * 构建带自定义名称的Virtual Thread执行器 {@link Executor}
     * 
     * <p>使用 {@link Thread.Builder} 创建具有自定义名称前缀的Virtual Thread执行器，
     * 便于在日志和监控中识别和跟踪Virtual Thread。</p>
     * 
     * <p><strong>与 {@code virtualThreadExecutor} 的区别：</strong></p>
     * <ul>
     *     <li>提供自定义的线程命名规则</li>
     *     <li>便于日志追踪和问题定位</li>
     *     <li>功能上与 {@code virtualThreadExecutor} 完全相同</li>
     * </ul>
     * 
     * @return 带自定义名称的Virtual Thread执行器实例
     * @since JDK 21
     */
    @Bean("namedVirtualThreadExecutor")
    public Executor namedVirtualThreadExecutor() {
        ThreadFactory virtualThreadFactory = Thread.ofVirtual()
                .name(VIRTUAL_THREAD_NAME_PREFIX, 0)
                .factory();
        return Executors.newThreadPerTaskExecutor(virtualThreadFactory);
    }
}
