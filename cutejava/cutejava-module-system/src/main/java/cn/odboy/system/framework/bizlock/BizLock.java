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

package cn.odboy.system.framework.bizlock;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 业务锁
 *
 * @author odboy
 * @date 2025-08-14
 */
@Inherited
@Documented
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BizLock {
    /**
     * 业务名称
     */
    String bizName() default "";

    /**
     * 锁key，支持SpEL表达式
     * 示例:
     * #user.id - 获取参数user的id属性
     * #p0 - 获取第一个参数
     * #root.methodName - 获取方法名
     */
    String key() default "";

    /**
     * 锁持有时间(秒)，默认60秒。锁的持有时间，超过这个时间锁会自动释放
     */
    long leaseTime() default 60;

    /**
     * 时间单位，默认为秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 获取锁失败后的异常信息
     */
    String errorMessage() default "操作太繁忙，请稍后再试";
}
