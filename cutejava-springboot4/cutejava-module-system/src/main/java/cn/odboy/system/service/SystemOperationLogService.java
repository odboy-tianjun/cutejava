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
package cn.odboy.system.service;

import cn.odboy.base.KitPageArgs;
import cn.odboy.system.dal.dataobject.SystemOperationLogTb;
import cn.odboy.system.dal.model.request.SystemQueryOperationLogArgs;
import cn.odboy.system.dal.mysql.SystemOperationLogMapper;
import cn.odboy.system.framework.permission.core.KitSecurityHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemOperationLogService {

  @Autowired
  private SystemOperationLogMapper systemOperationLogMapper;

  /**
   * 查询用户操作日志
   */
  public IPage<SystemOperationLogTb> searchUserLog(KitPageArgs<SystemQueryOperationLogArgs> pageArgs) {
    LambdaQueryWrapper<SystemOperationLogTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemOperationLogTb::getUsername, KitSecurityHelper.getCurrentUsername());
    Page<SystemOperationLogTb> page = new Page<>(pageArgs.getPage(), pageArgs.getSize());
    return systemOperationLogMapper.selectPage(page, wrapper);
  }
}
