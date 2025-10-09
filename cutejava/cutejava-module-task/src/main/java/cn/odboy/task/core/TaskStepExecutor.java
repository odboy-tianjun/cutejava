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
package cn.odboy.task.core;

import cn.odboy.framework.exception.web.BadRequestException;
import cn.odboy.task.dal.model.TaskTemplateNodeVo;
import org.quartz.JobDataMap;

import javax.validation.constraints.NotNull;

public interface TaskStepExecutor {
    /**
     * @param instanceDetailId 任务实例明细id
     * @param jobDataMap       执行参数
     * @param taskTemplateNode 任务节点信息
     * @param callback         执行成功回调
     */
    void execute(Long instanceDetailId, JobDataMap jobDataMap, TaskTemplateNodeVo taskTemplateNode, @NotNull TaskStepCallback callback) throws BadRequestException;
}
