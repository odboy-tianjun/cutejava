/*
 * Copyright 2021-2026 Odboy
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

import cn.odboy.task.constant.TaskStatusEnum;
import cn.odboy.task.dal.dataobject.TaskInstanceInfoTb;
import cn.odboy.task.dal.mysql.TaskInstanceInfoMapper;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.util.Date;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 任务实例
 * </p>
 *
 * @author codegen
 * @since 2025-09-26
 */
@Service
public class TaskInstanceInfoService {

  @Autowired
  private TaskInstanceInfoMapper taskInstanceInfoMapper;

  public TaskInstanceInfoTb getRunningById(Long id) {
    LambdaQueryWrapper<TaskInstanceInfoTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(TaskInstanceInfoTb::getStatus, TaskStatusEnum.Running.getCode());
    wrapper.orderByDesc(TaskInstanceInfoTb::getId);
    return taskInstanceInfoMapper.selectOne(wrapper);
  }

  @Transactional(rollbackFor = Exception.class)
  public void fastFailWithMessage(Long id, String errorMessage) {
    TaskInstanceInfoTb updRecord = new TaskInstanceInfoTb();
    updRecord.setId(id);
    updRecord.setFinishTime(new Date());
    updRecord.setStatus(TaskStatusEnum.Fail.getCode());
    updRecord.setErrorMessage(errorMessage);
    taskInstanceInfoMapper.updateById(updRecord);
  }

  @Transactional(rollbackFor = Exception.class)
  public void fastFailWithMessageData(Long id, String errorMessage, JobDataMap dataMap) {
    TaskInstanceInfoTb updRecord = new TaskInstanceInfoTb();
    updRecord.setId(id);
    updRecord.setFinishTime(new Date());
    updRecord.setStatus(TaskStatusEnum.Fail.getCode());
    updRecord.setErrorMessage(errorMessage);
    updRecord.setJobData(JSON.toJSONString(dataMap));
    taskInstanceInfoMapper.updateById(updRecord);
  }

  @Transactional(rollbackFor = Exception.class)
  public void fastSuccessWithData(Long id, JobDataMap dataMap) {
    TaskInstanceInfoTb updRecord = new TaskInstanceInfoTb();
    updRecord.setId(id);
    updRecord.setFinishTime(new Date());
    updRecord.setStatus(TaskStatusEnum.Success.getCode());
    updRecord.setJobData(JSON.toJSONString(dataMap));
    taskInstanceInfoMapper.updateById(updRecord);
  }

  public TaskInstanceInfoTb getLastRunningInstance(String contextName, String language, String envAlias,
      String changeType) {
    LambdaQueryWrapper<TaskInstanceInfoTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(TaskInstanceInfoTb::getContextName, contextName);
    wrapper.eq(TaskInstanceInfoTb::getLanguage, language);
    wrapper.eq(TaskInstanceInfoTb::getEnvAlias, envAlias);
    wrapper.eq(TaskInstanceInfoTb::getChangeType, changeType);
    wrapper.eq(TaskInstanceInfoTb::getStatus, TaskStatusEnum.Running.getCode());
    wrapper.orderByDesc(TaskInstanceInfoTb::getId);
    return taskInstanceInfoMapper.selectOne(wrapper);
  }

  public TaskInstanceInfoTb getLastHistoryInstance(String contextName, String language, String envAlias,
      String changeType) {
    LambdaQueryWrapper<TaskInstanceInfoTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(TaskInstanceInfoTb::getContextName, contextName);
    wrapper.eq(TaskInstanceInfoTb::getLanguage, language);
    wrapper.eq(TaskInstanceInfoTb::getEnvAlias, envAlias);
    wrapper.eq(TaskInstanceInfoTb::getChangeType, changeType);
    wrapper.orderByDesc(TaskInstanceInfoTb::getId);
    wrapper.last("LIMIT 1");
    return taskInstanceInfoMapper.selectOne(wrapper);
  }

  public void save(TaskInstanceInfoTb newInstance) {
    taskInstanceInfoMapper.insert(newInstance);
  }

  public void updateById(TaskInstanceInfoTb taskInstanceInfoTb) {
    taskInstanceInfoMapper.updateById(taskInstanceInfoTb);
  }

  public TaskInstanceInfoTb getById(Long id) {
    return taskInstanceInfoMapper.selectById(id);
  }
}
