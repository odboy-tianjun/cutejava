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

import cn.odboy.task.dal.model.TaskTemplateInfoVo;
import cn.odboy.task.dal.mysql.TaskTemplateInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 任务模板
 * </p>
 *
 * @author codegen
 * @since 2025-09-28
 */
@Service
public class TaskTemplateInfoService {

  @Autowired
  private TaskTemplateInfoMapper taskTemplateInfoMapper;

  public TaskTemplateInfoVo getTemplateInfoByECL(String envAlias, String contextName, String language,
      String changeType) {
    return taskTemplateInfoMapper.selectTemplateInfoByECL(envAlias, contextName, language, changeType);
  }
}
