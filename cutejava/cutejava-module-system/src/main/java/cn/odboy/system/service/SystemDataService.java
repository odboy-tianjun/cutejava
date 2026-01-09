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
import cn.odboy.system.dal.model.response.SystemDeptVo;
import cn.odboy.system.dal.model.response.SystemRoleVo;
import cn.odboy.system.dal.model.response.SystemUserVo;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据权限 Service
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
   * 获取数据权限
   *
   * @param user /
   * @return /
   */
  public List<Long> queryDeptIdByArgs(SystemUserVo user) {
    List<Long> deptIds = new ArrayList<>();
    // 查询用户角色
    List<SystemRoleVo> roleList = systemUserRoleService.queryRoleByUsersId(user.getId());
    // 获取对应的部门ID
    for (SystemRoleVo role : roleList) {
      SystemDataScopeEnum dataScopeEnum = SystemDataScopeEnum.find(role.getDataScope());
      switch (Objects.requireNonNull(dataScopeEnum)) {
        case THIS_LEVEL:
          deptIds.add(user.getDept().getId());
          break;
        case CUSTOMIZE:
          deptIds.addAll(this.queryCustomDataPermissionByArgs(deptIds, role));
          break;
        default:
          return new ArrayList<>();
      }
    }
    return deptIds.stream().distinct().collect(Collectors.toList());
  }

  /**
   * 获取自定义的数据权限
   *
   * @param deptIds 部门ID
   * @param role    角色
   * @return 数据权限ID
   */
  private List<Long> queryCustomDataPermissionByArgs(List<Long> deptIds, SystemRoleVo role) {
    List<SystemDeptTb> deptList = systemRoleDeptService.listDeptByRoleId(role.getId());
    for (SystemDeptTb dept : deptList) {
      deptIds.add(dept.getId());
      List<SystemDeptVo> deptChildren = systemDeptService.listDeptByPid(dept.getId());
      if (CollUtil.isNotEmpty(deptChildren)) {
        deptIds.addAll(systemDeptService.queryChildDeptIdByDeptIds(deptChildren));
      }
    }
    return deptIds;
  }
}
