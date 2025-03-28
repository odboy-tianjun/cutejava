package cn.odboy.thread;

import com.alibaba.ttl.TtlRunnable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 创建自定义的线程池
 */
@EnableAsync
@Configuration
public class AsyncExecutor implements AsyncConfigurer {

    public static int corePoolSize;

    public static int maxPoolSize;

    public static int keepAliveSeconds;

    public static int queueCapacity;

    @Value("${task.pool.core-pool-size}")
    public void setCorePoolSize(int corePoolSize) {
        AsyncExecutor.corePoolSize = corePoolSize;
    }

    @Value("${task.pool.max-pool-size}")
    public void setMaxPoolSize(int maxPoolSize) {
        AsyncExecutor.maxPoolSize = maxPoolSize;
    }

    @Value("${task.pool.keep-alive-seconds}")
    public void setKeepAliveSeconds(int keepAliveSeconds) {
        AsyncExecutor.keepAliveSeconds = keepAliveSeconds;
    }

    @Value("${task.pool.queue-capacity}")
    public void setQueueCapacity(int queueCapacity) {
        AsyncExecutor.queueCapacity = queueCapacity;
    }

    /**
     * 自定义线程池，用法 @Async
     *
     * @return Executor
     */
    @Override
    public Executor getAsyncExecutor() {
        // 自定义工厂
        ThreadFactory factory = r -> new Thread(r, "default-async-" + new AtomicInteger(1).getAndIncrement());
        // 自定义线程池
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveSeconds,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(queueCapacity), factory,
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * 自定义线程池，用法，注入到类中使用
     */
    @Bean("taskAsync")
    public ThreadPoolTaskExecutor taskAsync() {
        // 用法 private ThreadPoolTaskExecutor taskExecutor
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(AsyncExecutor.corePoolSize);
        executor.setMaxPoolSize(AsyncExecutor.maxPoolSize);
        executor.setQueueCapacity(AsyncExecutor.queueCapacity);
        executor.setThreadNamePrefix("task-async-");
        executor.setKeepAliveSeconds(AsyncExecutor.keepAliveSeconds);
        // DiscardOldestPolicy，抛弃最早的任务，将新任务加入队列。
        // AbortPolicy，拒绝执行新任务，并抛出异常。
        // CallerRunsPolicy，交由调用者线程执行新任务，如果调用者线程已关闭，则抛弃任务。
        // DiscardPolicy，直接抛弃新任务。
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        // 设置包装器
        executor.setTaskDecorator(TtlRunnable::get);
        executor.initialize();
        return executor;
    }
}
