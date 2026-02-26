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
package cn.odboy.framework.properties.model;

import cn.odboy.base.KitObject;
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
public class ThreadPoolSettingModel extends KitObject {

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
