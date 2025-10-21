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
package cn.odboy.task.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.odboy.framework.exception.web.BadRequestException;
import cn.odboy.task.constant.TaskStatusEnum;
import cn.odboy.task.dal.dataobject.TaskInstanceStepDetailTb;
import cn.odboy.task.dal.mysql.TaskInstanceStepDetailMapper;
import cn.odboy.task.service.TaskInstanceStepDetailService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 任务实例步骤明细 服务实现类
 * </p>
 *
 * @author codegen
 * @since 2025-09-28
 */
@Service
public class TaskInstanceStepDetailServiceImpl extends ServiceImpl<TaskInstanceStepDetailMapper, TaskInstanceStepDetailTb> implements TaskInstanceStepDetailService {

    @Override
    public void success(Long instanceDetailId, String stepDesc) {
        TaskInstanceStepDetailTb stepDetail = new TaskInstanceStepDetailTb();
        stepDetail.setInstanceDetailId(instanceDetailId);
        stepDetail.setStepDesc(stepDesc);
        stepDetail.setStepStatus(TaskStatusEnum.Success.getCode());
        save(stepDetail);
    }

    @Override
    public void fail(Long instanceDetailId, String stepDesc) {
        TaskInstanceStepDetailTb stepDetail = new TaskInstanceStepDetailTb();
        stepDetail.setInstanceDetailId(instanceDetailId);
        stepDetail.setStepDesc(stepDesc);
        stepDetail.setStepStatus(TaskStatusEnum.Fail.getCode());
        save(stepDetail);
        throw new BadRequestException(stepDesc);
    }

    @Override
    public void removeByInstanceDetailIds(List<Long> instanceDetailIds) {
        if (CollUtil.isEmpty(instanceDetailIds)) {
            return;
        }
        remove(new LambdaQueryWrapper<TaskInstanceStepDetailTb>()
                .in(TaskInstanceStepDetailTb::getInstanceDetailId, instanceDetailIds)
        );
    }
}
