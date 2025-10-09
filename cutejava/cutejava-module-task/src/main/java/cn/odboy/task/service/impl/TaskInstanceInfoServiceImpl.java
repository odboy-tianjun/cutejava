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

import cn.odboy.task.constant.TaskStatusEnum;
import cn.odboy.task.dal.dataobject.TaskInstanceInfoTb;
import cn.odboy.task.dal.mysql.TaskInstanceInfoMapper;
import cn.odboy.task.service.TaskInstanceInfoService;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.quartz.JobDataMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * 任务实例 服务实现类
 * </p>
 *
 * @author codegen
 * @since 2025-09-26
 */
@Service
public class TaskInstanceInfoServiceImpl extends ServiceImpl<TaskInstanceInfoMapper, TaskInstanceInfoTb> implements TaskInstanceInfoService {

    @Override
    public TaskInstanceInfoTb getRunningById(Long id) {
        return lambdaQuery()
                .eq(TaskInstanceInfoTb::getId, id)
                .eq(TaskInstanceInfoTb::getStatus, TaskStatusEnum.Running.getCode())
                .orderByDesc(TaskInstanceInfoTb::getId)
                .one();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void fastFailWithMessage(Long id, String errorMessage) {
        TaskInstanceInfoTb updRecord = new TaskInstanceInfoTb();
        updRecord.setId(id);
        updRecord.setFinishTime(new Date());
        updRecord.setStatus(TaskStatusEnum.Fail.getCode());
        updRecord.setErrorMessage(errorMessage);
        updateById(updRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void fastFailWithMessageData(Long id, String errorMessage, JobDataMap dataMap) {
        TaskInstanceInfoTb updRecord = new TaskInstanceInfoTb();
        updRecord.setId(id);
        updRecord.setFinishTime(new Date());
        updRecord.setStatus(TaskStatusEnum.Fail.getCode());
        updRecord.setErrorMessage(errorMessage);
        updRecord.setJobData(JSON.toJSONString(dataMap));
        updateById(updRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void fastSuccessWithData(Long id, JobDataMap dataMap) {
        TaskInstanceInfoTb updRecord = new TaskInstanceInfoTb();
        updRecord.setId(id);
        updRecord.setFinishTime(new Date());
        updRecord.setStatus(TaskStatusEnum.Success.getCode());
        updRecord.setJobData(JSON.toJSONString(dataMap));
        updateById(updRecord);
    }
}
