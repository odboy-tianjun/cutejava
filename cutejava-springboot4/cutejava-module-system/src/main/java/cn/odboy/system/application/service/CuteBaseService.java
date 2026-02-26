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
package cn.odboy.system.application.service;

import cn.odboy.system.dal.dataobject.SystemDeptTb;
import cn.odboy.system.dal.mysql.SystemDeptMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

public class CuteBaseService {

  @Autowired
  private SystemDeptMapper systemDeptMapper;

  /**
   * 查询启用的所有部门集合
   *
   * @return /
   */
  public List<SystemDeptTb> listEnabledDept() {
    LambdaQueryWrapper<SystemDeptTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemDeptTb::getEnabled, 1);
    return systemDeptMapper.selectList(wrapper);
  }
}
