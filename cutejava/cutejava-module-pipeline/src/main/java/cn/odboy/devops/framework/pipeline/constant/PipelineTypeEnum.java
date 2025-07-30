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
package cn.odboy.devops.framework.pipeline.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流水线类型
 */
@Getter
@AllArgsConstructor
public enum PipelineTypeEnum {
    BACKEND("backend", "后端"), FRONT("front", "前端"), MOBILE("mobile", "移动端"), ANDROID("pc", "PC端"), UN_SUPPORT("un_support", "不支持的类型");
    /**
     * 类型编码
     */
    private final String code;
    /**
     * 描述
     */
    private final String desc;

    public static PipelineTypeEnum getByCode(String code) {
        for (PipelineTypeEnum value : PipelineTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return UN_SUPPORT;
    }
}