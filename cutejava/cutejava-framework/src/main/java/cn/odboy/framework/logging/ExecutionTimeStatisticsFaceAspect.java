/*
 * Copyright 2021-2025 Odboy
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
package cn.odboy.framework.logging;

import cn.hutool.core.date.TimeInterval;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 执行时间统计
 *
 * @author odboy
 * @date 2025-05-07
 */
@Slf4j
@Aspect
@Component
public class ExecutionTimeStatisticsFaceAspect {

  @Around("@annotation(executionTimeStatisticsFace)")
  public Object aroundFace(ProceedingJoinPoint joinPoint, ExecutionTimeStatisticsFace executionTimeStatisticsFace)
      throws Throwable {
    TimeInterval timeInterval = new TimeInterval();
    try {
      Object proceed = joinPoint.proceed();
      log.info("[{}] 接口执行成功，耗时: {} ms", executionTimeStatisticsFace.description(),
          timeInterval.intervalMs());
      return proceed;
    } catch (Exception e) {
      if (executionTimeStatisticsFace.showErrLog()) {
        log.error("[{}] 接口执行失败，耗时: {} ms", executionTimeStatisticsFace.description(),
            timeInterval.intervalMs(), e);
      }
      throw e;
    }
  }
}
