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
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.system.dal.dataobject.SystemDeptTb;
import cn.odboy.system.dal.dataobject.SystemMenuTb;
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import cn.odboy.system.dal.dataobject.SystemUserRoleTb;
import cn.odboy.system.dal.model.SystemRoleVo;
import cn.odboy.system.dal.model.SystemUserVo;
import cn.odboy.system.dal.mysql.SystemRoleMapper;
import cn.odboy.system.dal.mysql.SystemUserRoleMapper;
import cn.odboy.system.framework.permission.core.KitSecurityHelper;
import cn.odboy.util.KitBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemUserRoleService {

  @Autowired
  private SystemRoleMapper systemRoleMapper;
  @Autowired
  private SystemUserRoleMapper systemUserRoleMapper;
  @Autowired
  private SystemRoleMenuService systemRoleMenuService;
  @Autowired
  private SystemRoleDeptService systemRoleDeptService;

  @Transactional(rollbackFor = Exception.class)
  public void batchInsertUserRole(Set<SystemRoleTb> roles, Long userId) {
    if (CollUtil.isNotEmpty(roles)) {
      List<SystemUserRoleTb> records = new ArrayList<>();
      for (SystemRoleTb role : roles) {
        SystemUserRoleTb record = new SystemUserRoleTb();
        record.setUserId(userId);
        record.setRoleId(role.getId());
        records.add(record);
      }
      systemUserRoleMapper.insert(records);
    }
  }

  @Transactional(rollbackFor = Exception.class)
  public void batchDeleteUserRole(Set<Long> userIds) {
    if (CollUtil.isNotEmpty(userIds)) {
      LambdaQueryWrapper<SystemUserRoleTb> wrapper = new LambdaQueryWrapper<>();
      wrapper.in(SystemUserRoleTb::getUserId, userIds);
      systemUserRoleMapper.delete(wrapper);
    }
  }

  /**
   * 转换为SystemRoleVo并关联查询菜单和部门信息
   *
   * @param role 角色基本信息
   * @return 包含关联信息的SystemRoleVo
   */
  public SystemRoleVo convertToRoleVo(SystemRoleTb role) {
    if (role == null) {
      return null;
    }
    SystemRoleVo roleVo = KitBeanUtil.copyToClass(role, SystemRoleVo.class);
    // 查询关联的菜单信息
    Set<SystemMenuTb> menus = systemRoleMenuService.queryMenuByRoleIds(
        Collections.singleton(role.getId()));
    roleVo.setMenus(menus);

    // 查询关联的部门信息
    List<SystemDeptTb> depts = systemRoleDeptService.listDeptByRoleId(role.getId());
    roleVo.setDepts(new LinkedHashSet<>(depts));

    return roleVo;
  }

  /**
   * 根据用户ID查询
   *
   * @param userId 用户ID
   * @return /
   */
  public List<SystemRoleVo> queryRoleByUsersId(Long userId) {
    // 查询用户角色关联
    LambdaQueryWrapper<SystemUserRoleTb> userRoleWrapper = new LambdaQueryWrapper<>();
    userRoleWrapper.eq(SystemUserRoleTb::getUserId, userId);
    List<SystemUserRoleTb> userRoles = systemUserRoleMapper.selectList(userRoleWrapper);
    if (CollUtil.isEmpty(userRoles)) {
      return new ArrayList<>();
    }
    Set<Long> roleIds = userRoles.stream().map(SystemUserRoleTb::getRoleId).collect(Collectors.toSet());
    List<SystemRoleTb> roles = systemRoleMapper.selectByIds(roleIds);
    return roles.stream().map(this::convertToRoleVo).collect(Collectors.toList());
  }

  /**
   * 根据角色查询角色级别
   *
   * @param roles /
   * @return /
   */
  public Integer getDeptLevelByRoles(Set<SystemRoleTb> roles) {
    if (CollUtil.isEmpty(roles)) {
      return Integer.MAX_VALUE;
    }
    Set<SystemRoleTb> roleSet = new HashSet<>();
    for (SystemRoleTb role : roles) {
      roleSet.add(systemRoleMapper.selectById(role.getId()));
    }
    return Collections.min(roleSet.stream().map(SystemRoleTb::getLevel).collect(Collectors.toList()));
  }

  /**
   * 如果当前用户的角色级别低于创建用户的角色级别，则抛出权限不足的错误
   *
   * @param args /
   */
  public void checkLevel(SystemUserVo args) {
    Integer currentLevel = Collections.min(
        this.queryRoleByUsersId(KitSecurityHelper.getCurrentUserId()).stream()
            .map(SystemRoleTb::getLevel).collect(Collectors.toList()));
    Integer optLevel = this.getDeptLevelByRoles(args.getRoles());
    if (currentLevel > optLevel) {
      throw new BadRequestException("角色权限不足");
    }
  }

  public List<SystemUserRoleTb> listUserRoleByUserId(Long userId) {
    LambdaQueryWrapper<SystemUserRoleTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemUserRoleTb::getUserId, userId);
    return systemUserRoleMapper.selectList(wrapper);
  }

  public long countUserByRoleIds(Set<Long> ids) {
    if (CollUtil.isEmpty(ids)) {
      return 0L;
    }
    LambdaQueryWrapper<SystemUserRoleTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.in(SystemUserRoleTb::getRoleId, ids);
    return systemUserRoleMapper.selectCount(wrapper);
  }
}
