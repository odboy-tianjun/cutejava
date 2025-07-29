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
package cn.odboy.devops.dal.model;

import cn.odboy.base.CsObject;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 启动流水线 参数
 *
 * @author odboy
 * @date 2025-07-28
 */
@Getter
@Setter
public class StartPipelineArgs extends CsObject {
    @NotNull(message = "模板id必填")
    private Long templateId;
    @NotBlank(message = "实例名称必填")
    private String instanceName;
    @NotBlank(message = "上下文名称必填")
    private String contextName;
    @NotBlank(message = "环境编码必填")
    private String env;
}
