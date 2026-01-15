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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.system.dal.dataobject.SystemMenuTb;
import cn.odboy.system.dal.dataobject.SystemRoleMenuTb;
import cn.odboy.system.dal.mysql.SystemRoleMenuMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SystemRoleMenuService {

  @Autowired
  private SystemRoleMenuMapper systemRoleMenuMapper;

  /**
   * 删除角色菜单关联 -> TestPassed
   *
   * @param roleId 角色id
   */
  @Transactional(rollbackFor = Exception.class)
  public void deleteRoleMenuByRoleId(Long roleId) {
    LambdaQueryWrapper<SystemRoleMenuTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemRoleMenuTb::getRoleId, roleId);
    systemRoleMenuMapper.delete(wrapper);
  }

  /**
   * 批量绑定角色菜单关联 -> TestPassed
   *
   * @param menus  菜单集合
   * @param roleId 角色id
   */
  @Transactional(rollbackFor = Exception.class)
  public void batchInsertRoleMenu(Set<SystemMenuTb> menus, Long roleId) {
    if (CollUtil.isNotEmpty(menus)) {
      List<SystemRoleMenuTb> records = new ArrayList<>();
      for (SystemMenuTb menu : menus) {
        SystemRoleMenuTb record = new SystemRoleMenuTb();
        record.setMenuId(menu.getId());
        record.setRoleId(roleId);
        records.add(record);
      }
      systemRoleMenuMapper.insert(records);
    }
  }

  /**
   * 批量删除角色菜单关联 -> TestPassed
   *
   * @param roleIds 角色id集合
   */
  @Transactional(rollbackFor = Exception.class)
  public void batchDeleteRoleMenu(Set<Long> roleIds) {
    if (CollUtil.isNotEmpty(roleIds)) {
      LambdaQueryWrapper<SystemRoleMenuTb> wrapper = new LambdaQueryWrapper<>();
      wrapper.in(SystemRoleMenuTb::getRoleId, roleIds);
      systemRoleMenuMapper.delete(wrapper);
    }
  }

  /**
   * @param roleIds 角色id集合 -> TestPassed
   * @return /
   */
  public List<SystemMenuTb> listMenuByRoleIds(Set<Long> roleIds, boolean hasButton) {
    if (CollUtil.isEmpty(roleIds)) {
      return new ArrayList<>();
    }
//    LambdaQueryWrapper<SystemRoleMenuTb> roleMenuWrapper = new LambdaQueryWrapper<>();
//    roleMenuWrapper.in(SystemRoleMenuTb::getRoleId, roleIds);
//    List<Long> menuIds = systemRoleMenuMapper.selectList(roleMenuWrapper).stream().map(SystemRoleMenuTb::getMenuId)
//        .collect(Collectors.toList());
//    if (CollUtil.isEmpty(menuIds)) {
//      return new ArrayList<>();
//    }
//    LambdaQueryWrapper<SystemMenuTb> menuWrapper = new LambdaQueryWrapper<>();
//    menuWrapper.in(SystemMenuTb::getId, menuIds);
//    // 排除"按钮"
//    menuWrapper.ne(SystemMenuTb::getType, 2);
//    menuWrapper.orderByAsc(SystemMenuTb::getMenuSort);
//    return systemMenuMapper.selectList(menuWrapper);
    if (hasButton){
      return systemRoleMenuMapper.listMenuWithRoleIds(roleIds);
    }
    return systemRoleMenuMapper.listMenuWithRoleIdsNeButton(roleIds);
  }

  /**
   * 批量删除菜单角色关联 -> TestPassed
   *
   * @param menuIds 菜单id集合
   */
  @Transactional(rollbackFor = Exception.class)
  public void deleteRoleMenuByMenuIds(Set<Long> menuIds) {
    if (CollUtil.isEmpty(menuIds)) {
      return;
    }
    LambdaQueryWrapper<SystemRoleMenuTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.in(SystemRoleMenuTb::getMenuId, menuIds);
    systemRoleMenuMapper.delete(wrapper);
  }

  /**
   * 根据角色id集合查询菜单权限 -> TestPassed
   *
   * @param roleIds 角色id集合
   * @return /
   */
  public List<String> listMenuPermissionByRoleIds(Set<Long> roleIds) {
    Set<String> permissions = systemRoleMenuMapper.listMenuPermissionByRoleIds(roleIds);
    return permissions.stream().filter(StrUtil::isNotBlank).collect(Collectors.toList());
  }
}
