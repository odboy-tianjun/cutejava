/*
 * Copyright 2021-2026 Odboy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.odboy.framework.server.config;

import cn.odboy.framework.properties.AppProperties;
import cn.odboy.framework.properties.model.ThreadPoolSettingModel;
import com.alibaba.ttl.TtlRunnable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 创建自定义的线程池
 */
@EnableAsync
@Configuration
public class AsyncTheadPoolConfig implements AsyncConfigurer {

  @Autowired
  private AppProperties properties;

  /**
   * 自定义线程池, 用法 @Async
   *
   * @return Executor
   */
  @Override
  public Executor getAsyncExecutor() {
    ThreadPoolSettingModel asyncTaskPool = properties.getAsyncTaskPool();
    // 自定义工厂
    ThreadFactory factory = r -> new Thread(r, "DefaultAsync-" + new AtomicInteger(1).getAndIncrement());
    // 自定义线程池
    return new ThreadPoolExecutor(asyncTaskPool.getCorePoolSize(), asyncTaskPool.getMaxPoolSize(),
        asyncTaskPool.getKeepAliveSeconds(), TimeUnit.SECONDS,
        new ArrayBlockingQueue<>(asyncTaskPool.getQueueCapacity()), factory,
        new ThreadPoolExecutor.CallerRunsPolicy());
  }

  /**
   * 自定义线程池, 用法, 注入到类中使用
   */
  @Bean("taskAsync")
  public ThreadPoolTaskExecutor taskAsync() {
    ThreadPoolSettingModel asyncTaskPool = properties.getAsyncTaskPool();
    // 用法 private ThreadPoolTaskExecutor taskExecutor
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(asyncTaskPool.getCorePoolSize());
    executor.setMaxPoolSize(asyncTaskPool.getMaxPoolSize());
    executor.setQueueCapacity(asyncTaskPool.getQueueCapacity());
    executor.setThreadNamePrefix("TaskAsync-");
    executor.setKeepAliveSeconds(asyncTaskPool.getKeepAliveSeconds());
    // DiscardOldestPolicy，抛弃最早的任务，将新任务加入队列。
    // AbortPolicy，拒绝执行新任务，并抛出异常。
    // CallerRunsPolicy，交由调用者线程执行新任务，如果调用者线程已关闭，则抛弃任务。
    // DiscardPolicy，直接抛弃新任务。
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
    // 设置包装器
    executor.setTaskDecorator(TtlRunnable::get);
    executor.initialize();
    return executor;
  }

  @Bean("longTimeTaskAsync")
  public ThreadPoolTaskExecutor longTimeTaskAsync() {
    ThreadPoolSettingModel asyncTaskPool = properties.getAsyncTaskPool();
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(asyncTaskPool.getCorePoolSize());
    executor.setMaxPoolSize(asyncTaskPool.getMaxPoolSize());
    executor.setQueueCapacity(asyncTaskPool.getQueueCapacity());
    executor.setThreadNamePrefix("LongTimeTaskAsync-");
    executor.setKeepAliveSeconds(asyncTaskPool.getKeepAliveSeconds());
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
    // 设置包装器
    executor.setTaskDecorator(TtlRunnable::get);
    executor.initialize();
    return executor;
  }
}
