package cn.odboy.core.framework.properties.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 线程池 配置
 *
 * @author odboy
 * @date 2025-04-13
 */
@Getter
@Setter
public class ThreadPoolSettingModel {
    /**
     * 核心线程池大小
     */
    private int corePoolSize;
    /**
     * 最大线程数
     */
    private int maxPoolSize;
    /**
     * 活跃时间
     */
    private int keepAliveSeconds;
    /**
     * 队列容量
     */
    private int queueCapacity;
}
