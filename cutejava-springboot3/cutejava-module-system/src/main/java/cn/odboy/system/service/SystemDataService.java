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
import cn.odboy.system.constant.SystemDataScopeEnum;
import cn.odboy.system.dal.dataobject.SystemDeptTb;
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import cn.odboy.system.dal.model.response.SystemUserVo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据权限
 *
 * @author odboy
 */
@Service
public class SystemDataService {

  @Autowired
  private SystemUserRoleService systemUserRoleService;
  @Autowired
  private SystemDeptService systemDeptService;
  @Autowired
  private SystemRoleDeptService systemRoleDeptService;

  /**
   * 获取数据权限 -> TestPassed
   *
   * @param user /
   * @return /
   */
  public List<Long> queryDeptIdByArgs(SystemUserVo user) {
    List<Long> deptIds = new ArrayList<>();
    // 查询用户角色
    Set<SystemRoleTb> roleList = systemUserRoleService.listUserRoleByUserId(user.getId());
    // 获取对应的部门ID
    for (SystemRoleTb role : roleList) {
      SystemDataScopeEnum dataScopeEnum = SystemDataScopeEnum.find(role.getDataScope());
      switch (Objects.requireNonNull(dataScopeEnum)) {
        case THIS_LEVEL:
          Long currentDeptId = user.getDept().getId();
          deptIds.add(currentDeptId);
          break;
        case CUSTOMIZE:
          // 优化 283ms -> 51 ms
          List<Long> customDeptIds = this.queryCustomDataPermissionByArgs(deptIds, role.getId());
          deptIds.addAll(customDeptIds);
          break;
        default:
          return new ArrayList<>();
      }
    }
    return deptIds.stream().distinct().collect(Collectors.toList());
  }

  /**
   * 获取自定义的数据权限 -> TestPassed
   *
   * @param deptIds 部门ID（外部数据）
   * @param roleId  角色ID
   * @return 数据权限ID（所有部门ID数据）
   */
  private List<Long> queryCustomDataPermissionByArgs(List<Long> deptIds, Long roleId) {
    Set<SystemDeptTb> deptList = systemRoleDeptService.listUserDeptByRoleId(roleId);
    Map<Long, List<SystemDeptTb>> deptPidMap =
        systemDeptService.listPidNonNull().stream().collect(Collectors.groupingBy(SystemDeptTb::getPid));
    for (SystemDeptTb dept : deptList) {
      deptIds.add(dept.getId());
      List<SystemDeptTb> deptChildren = deptPidMap.getOrDefault(dept.getId(), null);
      if (CollUtil.isNotEmpty(deptChildren)) {
        deptIds.addAll(systemDeptService.queryChildDeptIdByDeptIds(deptChildren, deptPidMap));
      }
    }
    return deptIds;
  }
}
