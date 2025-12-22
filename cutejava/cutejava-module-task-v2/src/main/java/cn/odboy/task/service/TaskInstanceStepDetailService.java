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
package cn.odboy.task.service;

import cn.hutool.core.collection.CollUtil;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.task.constant.TaskStatusEnum;
import cn.odboy.task.dal.dataobject.TaskInstanceStepDetailTb;
import cn.odboy.task.dal.mysql.TaskInstanceStepDetailMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 任务实例步骤明细
 * </p>
 *
 * @author codegen
 * @since 2025-09-28
 */
@Service
public class TaskInstanceStepDetailService {
    @Autowired private TaskInstanceStepDetailMapper taskInstanceStepDetailMapper;

    /**
     * 步骤执行成功
     *
     * @param instanceDetailId 任务实例明细id
     * @param stepDesc         步骤描述
     */
    public void success(Long instanceDetailId, String stepDesc) {
        TaskInstanceStepDetailTb stepDetail = new TaskInstanceStepDetailTb();
        stepDetail.setInstanceDetailId(instanceDetailId);
        stepDetail.setStepDesc(stepDesc);
        stepDetail.setStepStatus(TaskStatusEnum.Success.getCode());
        taskInstanceStepDetailMapper.insert(stepDetail);
    }

    /**
     * 步骤执行失败
     *
     * @param instanceDetailId 任务实例明细id
     * @param stepDesc         步骤描述
     */
    public void fail(Long instanceDetailId, String stepDesc) {
        TaskInstanceStepDetailTb stepDetail = new TaskInstanceStepDetailTb();
        stepDetail.setInstanceDetailId(instanceDetailId);
        stepDetail.setStepDesc(stepDesc);
        stepDetail.setStepStatus(TaskStatusEnum.Fail.getCode());
        taskInstanceStepDetailMapper.insert(stepDetail);
        throw new BadRequestException(stepDesc);
    }

    /**
     * 根据明细ID删除步骤明细
     *
     * @param instanceDetailIds 任务实例明细id集合
     */
    public void removeByInstanceDetailIds(List<Long> instanceDetailIds) {
        if (CollUtil.isEmpty(instanceDetailIds)) {
            return;
        }
        LambdaQueryWrapper<TaskInstanceStepDetailTb> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(TaskInstanceStepDetailTb::getInstanceDetailId, instanceDetailIds);
        taskInstanceStepDetailMapper.delete(wrapper);
    }
}
