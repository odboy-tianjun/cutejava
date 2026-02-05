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

package cn.odboy.framework.quartz.config;

import lombok.extern.slf4j.Slf4j;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * 定时任务配置
 */
@Slf4j
@Configuration
@Scope("singleton")
public class QuartzConfig {

  /**
   * 解决Job中注入Spring Bean为null的问题
   */
  @Component("quartzJobFactory")
  public static class QuartzJobFactory extends AdaptableJobFactory {

    private final AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Autowired
    public QuartzJobFactory(AutowireCapableBeanFactory autowireCapableBeanFactory) {
      this.autowireCapableBeanFactory = autowireCapableBeanFactory;
    }

    @NonNull
    @Override
    protected Object createJobInstance(@NonNull TriggerFiredBundle triggerFiredBundle) throws Exception {
      try {
        // 调用父类的方法, 把Job注入Spring中
        Object jobInstance = super.createJobInstance(triggerFiredBundle);
        autowireCapableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
      } catch (Exception e) {
        log.error("Job注入Spring失败, {}", triggerFiredBundle, e);
        throw e;
      }
    }
  }
}
