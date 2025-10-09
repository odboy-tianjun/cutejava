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
import cn.odboy.task.dal.dataobject.TaskInstanceDetailTb;
import cn.odboy.task.dal.mysql.TaskInstanceDetailMapper;
import cn.odboy.task.service.TaskInstanceDetailService;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.quartz.JobDataMap;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 任务实例明细 服务实现类
 * </p>
 *
 * @author codegen
 * @since 2025-09-26
 */
@Service
public class TaskInstanceDetailServiceImpl extends ServiceImpl<TaskInstanceDetailMapper, TaskInstanceDetailTb> implements TaskInstanceDetailService {

    @Override
    public void fastFailWithInfo(Long instanceId, String code, String executeInfo) {
        TaskInstanceDetailTb updRecord = new TaskInstanceDetailTb();
        updRecord.setExecuteInfo(executeInfo);
        updRecord.setExecuteStatus(TaskStatusEnum.Fail.getCode());
        lambdaUpdate()
                .eq(TaskInstanceDetailTb::getInstanceId, instanceId)
                .eq(TaskInstanceDetailTb::getBizCode, code)
                .update(updRecord);
    }

    @Override
    public void fastSuccessWithInfo(Long instanceId, String code, String executeInfo) {
        TaskInstanceDetailTb updRecord = new TaskInstanceDetailTb();
        updRecord.setFinishTime(new Date());
        updRecord.setExecuteInfo(executeInfo);
        updRecord.setExecuteStatus(TaskStatusEnum.Success.getCode());
        lambdaUpdate()
                .eq(TaskInstanceDetailTb::getInstanceId, instanceId)
                .eq(TaskInstanceDetailTb::getBizCode, code)
                .update(updRecord);
    }

    @Override
    public void fastStart(Long instanceId, String code, JobDataMap dataMap) {
        TaskInstanceDetailTb updRecord = new TaskInstanceDetailTb();
        updRecord.setStartTime(new Date());
        updRecord.setExecuteInfo("运行中");
        updRecord.setExecuteParams(JSON.toJSONString(dataMap));
        updRecord.setExecuteStatus(TaskStatusEnum.Running.getCode());
        lambdaUpdate()
                .eq(TaskInstanceDetailTb::getInstanceId, instanceId)
                .eq(TaskInstanceDetailTb::getBizCode, code)
                .update(updRecord);
    }
}
