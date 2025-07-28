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
package cn.odboy.devops.constant.pipeline;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流水线状态枚举
 */
@Getter
@AllArgsConstructor
public enum PipelineStatusEnum {
    PENDING("pending", "#C0C4CC", "未开始"),
    RUNNING("running", "#409EFF", "运行中"),
    SUCCESS("success", "#67C23A", "执行成功"),
    FAIL("fail", "#F56C6C", "执行失败");
    /**
     * 状态码
     */
    private final String code;
    /**
     * 颜色
     */
    private final String color;
    /**
     * 描述
     */
    private final String desc;
}