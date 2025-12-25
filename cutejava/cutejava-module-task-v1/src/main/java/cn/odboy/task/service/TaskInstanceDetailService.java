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
import cn.odboy.task.constant.TaskStatusEnum;
import cn.odboy.task.dal.dataobject.TaskInstanceDetailTb;
import cn.odboy.task.dal.mysql.TaskInstanceDetailMapper;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import java.util.Date;
import java.util.List;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 任务实例明细
 * </p>
 *
 * @author codegen
 * @since 2025-09-28
 */
@Service
public class TaskInstanceDetailService {

  @Autowired
  private TaskInstanceDetailMapper taskInstanceDetailMapper;

  public void fastFailWithInfo(Long instanceId, String code, String executeInfo) {
    LambdaUpdateWrapper<TaskInstanceDetailTb> wrapper = new LambdaUpdateWrapper<>();
    wrapper.eq(TaskInstanceDetailTb::getInstanceId, instanceId);
    wrapper.eq(TaskInstanceDetailTb::getBizCode, code);
    wrapper.set(TaskInstanceDetailTb::getFinishTime, new Date());
    wrapper.set(TaskInstanceDetailTb::getExecuteInfo, executeInfo);
    wrapper.set(TaskInstanceDetailTb::getExecuteStatus, TaskStatusEnum.Fail.getCode());
    taskInstanceDetailMapper.update(null, wrapper);
  }

  public void fastSuccess(Long instanceId, String code) {
    LambdaUpdateWrapper<TaskInstanceDetailTb> wrapper = new LambdaUpdateWrapper<>();
    wrapper.eq(TaskInstanceDetailTb::getInstanceId, instanceId);
    wrapper.eq(TaskInstanceDetailTb::getBizCode, code);
    wrapper.set(TaskInstanceDetailTb::getFinishTime, new Date());
    wrapper.set(TaskInstanceDetailTb::getExecuteInfo, TaskStatusEnum.Success.getName());
    wrapper.set(TaskInstanceDetailTb::getExecuteStatus, TaskStatusEnum.Success.getCode());
    taskInstanceDetailMapper.update(null, wrapper);
  }

  public void fastSuccessWithInfo(Long instanceId, String code, String executeInfo) {
    LambdaUpdateWrapper<TaskInstanceDetailTb> wrapper = new LambdaUpdateWrapper<>();
    wrapper.eq(TaskInstanceDetailTb::getInstanceId, instanceId);
    wrapper.eq(TaskInstanceDetailTb::getBizCode, code);
    wrapper.set(TaskInstanceDetailTb::getFinishTime, new Date());
    wrapper.set(TaskInstanceDetailTb::getExecuteInfo,
        executeInfo == null ? TaskStatusEnum.Success.getName() : executeInfo);
    wrapper.set(TaskInstanceDetailTb::getExecuteStatus, TaskStatusEnum.Success.getCode());
    taskInstanceDetailMapper.update(null, wrapper);
  }

  public void fastStart(Long instanceId, String code, JobDataMap dataMap) {
    LambdaUpdateWrapper<TaskInstanceDetailTb> wrapper = new LambdaUpdateWrapper<>();
    wrapper.eq(TaskInstanceDetailTb::getInstanceId, instanceId);
    wrapper.eq(TaskInstanceDetailTb::getBizCode, code);
    wrapper.set(TaskInstanceDetailTb::getStartTime, new Date());
    wrapper.set(TaskInstanceDetailTb::getExecuteInfo, TaskStatusEnum.Running.getName());
    wrapper.set(TaskInstanceDetailTb::getExecuteParams, JSON.toJSONString(dataMap));
    wrapper.set(TaskInstanceDetailTb::getExecuteStatus, TaskStatusEnum.Running.getCode());
    taskInstanceDetailMapper.update(null, wrapper);
  }

  public List<TaskInstanceDetailTb> queryByInstanceIdAndBizCodeList(Long instanceId, List<String> bizCodeList) {
    if (CollUtil.isEmpty(bizCodeList)) {
      return CollUtil.newArrayList();
    }
    LambdaQueryWrapper<TaskInstanceDetailTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(TaskInstanceDetailTb::getInstanceId, instanceId);
    wrapper.in(TaskInstanceDetailTb::getBizCode, bizCodeList);
    return taskInstanceDetailMapper.selectList(wrapper);
  }

  public List<TaskInstanceDetailTb> queryByInstanceId(Long instanceId) {
    LambdaUpdateWrapper<TaskInstanceDetailTb> wrapper = new LambdaUpdateWrapper<>();
    wrapper.eq(TaskInstanceDetailTb::getInstanceId, instanceId);
    wrapper.orderByAsc(TaskInstanceDetailTb::getId);
    return taskInstanceDetailMapper.selectList(wrapper);
  }

  public void saveBatch(List<TaskInstanceDetailTb> taskInstanceDetails) {
    taskInstanceDetailMapper.insert(taskInstanceDetails);
  }

  public void removeByIds(List<Long> taskInstanceDetailIds) {
    taskInstanceDetailMapper.deleteByIds(taskInstanceDetailIds);
  }
}
