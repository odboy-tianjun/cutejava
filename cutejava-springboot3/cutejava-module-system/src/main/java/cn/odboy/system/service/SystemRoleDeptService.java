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
package cn.odboy.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.odboy.system.dal.dataobject.SystemDeptTb;
import cn.odboy.system.dal.dataobject.SystemRoleDeptTb;
import cn.odboy.system.dal.mysql.SystemDeptMapper;
import cn.odboy.system.dal.mysql.SystemRoleDeptMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemRoleDeptService {

  @Autowired
  private SystemRoleDeptMapper systemRoleDeptMapper;
  @Autowired
  private SystemDeptMapper systemDeptMapper;

  @Transactional(rollbackFor = Exception.class)
  public void batchDeleteRoleDept(Set<Long> roleIds) {
    if (CollUtil.isNotEmpty(roleIds)) {
      LambdaQueryWrapper<SystemRoleDeptTb> wrapper = new LambdaQueryWrapper<>();
      wrapper.in(SystemRoleDeptTb::getRoleId, roleIds);
      systemRoleDeptMapper.delete(wrapper);
    }
  }

  @Transactional(rollbackFor = Exception.class)
  public void batchInsertRoleDept(Set<SystemDeptTb> depts, Long id) {
    if (CollUtil.isNotEmpty(depts)) {
      List<SystemRoleDeptTb> records = new ArrayList<>();
      for (SystemDeptTb dept : depts) {
        SystemRoleDeptTb record = new SystemRoleDeptTb();
        record.setRoleId(id);
        record.setDeptId(dept.getId());
        records.add(record);
      }
      systemRoleDeptMapper.insert(records);
    }
  }

  /**
   * 根据角色id查询
   *
   * @param roleId /
   * @return /
   */
  public List<SystemDeptTb> selectDeptByRoleId(Long roleId) {
    LambdaQueryWrapper<SystemRoleDeptTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemRoleDeptTb::getRoleId, roleId);
    List<SystemRoleDeptTb> systemRoleDeptTbs = systemRoleDeptMapper.selectList(wrapper);
    if (systemRoleDeptTbs.isEmpty()) {
      return new ArrayList<>();
    }
    List<Long> deptIds = systemRoleDeptTbs.stream().map(SystemRoleDeptTb::getDeptId).collect(Collectors.toList());
    return systemDeptMapper.selectByIds(deptIds);
  }
}
