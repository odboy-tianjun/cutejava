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

/**
 * 流水线节点重试 参数
 *
 * @author odboy
 * @date 2025-07-28
 */
@Getter
@Setter
public class RestartPipelineNodeArgs extends CsObject {
    @NotBlank(message = "流水线实例id必填")
    private String instanceId;
    @NotBlank(message = "重试节点code必填")
    private String code;
}
