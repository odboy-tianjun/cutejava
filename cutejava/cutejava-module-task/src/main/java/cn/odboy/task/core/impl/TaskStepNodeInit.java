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
package cn.odboy.task.core.impl;

import cn.odboy.framework.exception.web.BadRequestException;
import cn.odboy.task.core.TaskStepCallback;
import cn.odboy.task.core.TaskStepExecutor;
import cn.odboy.task.dal.model.TaskTemplateNodeVo;
import cn.odboy.task.service.TaskInstanceStepDetailService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TaskStepNodeInit implements TaskStepExecutor {
    @Autowired
    private TaskInstanceStepDetailService service;

    @Override
    public void execute(Long instanceDetailId, JobDataMap jobDataMap, TaskTemplateNodeVo taskTemplateNode, TaskStepCallback callback) throws BadRequestException {
        callback.onStart();
        service.success(instanceDetailId, "开始初始化");
        service.success(instanceDetailId, "初始化完毕后，放入了一些参数");
        jobDataMap.put("ExcuseMe", "Hello World!");
        callback.onFinish("执行成功");
    }
}
