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

/**
 * 动态查询 枚举
 */
public enum CsMpQTypeEnum {
    /**
     * 相等
     */
    EQUAL
    /**
     * 大于等于
     */
    , GREATER_THAN
    /**
     * 小于等于
     */
    , LESS_THAN
    /**
     * 中模糊查询
     */
    , INNER_LIKE
    /**
     * 左模糊查询
     */
    , LEFT_LIKE
    /**
     * 右模糊查询
     */
    , RIGHT_LIKE
    /**
     * 小于
     */
    , LESS_THAN_NQ
    /**
     * 包含
     */
    , IN
    /**
     * 不包含
     */
    , NOT_IN
    /**
     * 不等于
     */
    , NOT_EQUAL
    /**
     * between(注：被注解的属性一定得是数组或者集合, 长度大于等于2)
     */
    , BETWEEN
    /**
     * 不为空
     */
    , NOT_NULL
    /**
     * 为空
     */
    , IS_NULL
}
