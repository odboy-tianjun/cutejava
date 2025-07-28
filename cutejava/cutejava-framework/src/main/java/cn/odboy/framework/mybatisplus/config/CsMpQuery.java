/*
 *  Copyright 2021-2025 Odboy
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package cn.odboy.framework.mybatisplus.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 查询描述
 *
 * @date 2023-11-25
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsMpQuery {
    /**
     * 基本对象的属性名
     */
    String propName() default "";

    /**
     * 查询方式
     */
    CsMpQTypeEnum type() default CsMpQTypeEnum.EQUAL;

    /**
     * 连接查询的属性名, 如User类中的dept
     */
    String joinName() default "";

    /**
     * 默认左连接
     */
    Join join() default Join.LEFT;

    /**
     * 多字段模糊搜索, 仅支持String类型字段, 多个用逗号隔开, 如@MpQuery(blurry = "email,username")
     */
    String blurry() default "";

    /**
     * 适用于简单连接查询, 复杂的请自定义该注解, 或者使用sql查询
     */
    enum Join {
        /**
         * 左连接
         */
        LEFT,
        /**
         * 右连接
         */
        RIGHT,
        /**
         * 内连接
         */
        INNER
    }
}
