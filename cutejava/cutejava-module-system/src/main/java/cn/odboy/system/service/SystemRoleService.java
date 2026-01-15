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
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.odboy.base.KitPageResult;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.system.dal.dataobject.SystemMenuTb;
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import cn.odboy.system.dal.model.export.SystemRoleExportRowVo;
import cn.odboy.system.dal.model.request.SystemCreateRoleArgs;
import cn.odboy.system.dal.model.request.SystemQueryRoleArgs;
import cn.odboy.system.dal.model.response.SystemRoleCodeVo;
import cn.odboy.system.dal.model.response.SystemRoleVo;
import cn.odboy.system.dal.model.response.SystemUserVo;
import cn.odboy.system.dal.mysql.SystemRoleMapper;
import cn.odboy.system.framework.permission.core.KitSecurityHelper;
import cn.odboy.util.KitBeanUtil;
import cn.odboy.util.KitPageUtil;
import cn.odboy.util.xlsx.KitExcelExporter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SystemRoleService {

  @Autowired
  private SystemRoleMapper systemRoleMapper;
  @Autowired
  private SystemRoleMenuService systemRoleMenuService;
  @Autowired
  private SystemRoleDeptService systemRoleDeptService;
  @Autowired
  private SystemUserRoleService systemUserRoleService;

  /**
   * 创建 -> TestPassed
   *
   * @param args /
   */
  @Transactional(rollbackFor = Exception.class)
  public void saveRole(SystemCreateRoleArgs args) {
    checkRoleLevels(args.getLevel());
    if (this.existRoleWithName(args.getName())) {
      throw new BadRequestException("角色名称已存在");
    }
    SystemRoleTb roleTb = KitBeanUtil.copyToClass(args, SystemRoleTb.class);
    systemRoleMapper.insert(roleTb);
    // 判断是否有部门数据, 若有, 则需创建关联
    if (CollUtil.isNotEmpty(args.getDepts())) {
      systemRoleDeptService.batchInsertRoleDept(args.getDepts(), roleTb.getId());
    }
  }

  private boolean existRoleWithName(String name) {
    LambdaQueryWrapper<SystemRoleTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemRoleTb::getName, name);
    return systemRoleMapper.exists(wrapper);
  }

  private boolean existRoleWithNameNeSelf(String name, Long roleId) {
    LambdaQueryWrapper<SystemRoleTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemRoleTb::getName, name);
    wrapper.eq(SystemRoleTb::getId, roleId);
    return systemRoleMapper.exists(wrapper);
  }

  /**
   * 修改 -> TestPassed
   *
   * @param args /
   */
  @Transactional(rollbackFor = Exception.class)
  public void updateRoleById(SystemRoleVo args) {
    checkRoleLevels(args.getLevel());
    SystemRoleTb role = systemRoleMapper.selectById(args.getId());
    if (this.existRoleWithNameNeSelf(args.getName(), role.getId())) {
      throw new BadRequestException("角色名称已存在");
    }
    role.setName(args.getName());
    role.setDescription(args.getDescription());
    role.setDataScope(args.getDataScope());
    role.setLevel(args.getLevel());
    // 更新
    systemRoleMapper.updateById(role);
    // 删除关联部门数据
    systemRoleDeptService.batchDeleteRoleDept(Collections.singleton(args.getId()));
    // 判断是否有部门数据, 若有, 则需更新关联
    if (CollUtil.isNotEmpty(args.getDepts())) {
      systemRoleDeptService.batchInsertRoleDept(args.getDepts(), args.getId());
    }
  }

  /**
   * 修改绑定的菜单 -> TestPassed
   *
   * @param args /
   */
  @Transactional(rollbackFor = Exception.class)
  public void updateBindMenuById(SystemRoleVo args) {
    SystemRoleTb role = systemRoleMapper.selectById(args.getId());
    checkRoleLevels(role.getLevel());
    // 更新菜单
    systemRoleMenuService.deleteRoleMenuByRoleId(role.getId());
    Set<SystemMenuTb> menus = args.getMenus();
    if (CollUtil.isNotEmpty(menus)) {
      systemRoleMenuService.batchInsertRoleMenu(menus, role.getId());
    }
  }

  /**
   * 删除 -> TestPassed
   *
   * @param ids /
   */
  @Transactional(rollbackFor = Exception.class)
  public void deleteRoleByIds(Set<Long> ids) {
    List<Integer> roleLevels = systemRoleMapper.listRoleLevelByRoleIds(new ArrayList<>(ids));
    for (Integer roleLevel : roleLevels) {
      checkRoleLevels(roleLevel);
    }
    // 验证是否被用户关联
    this.verifyBindRelationByIds(ids);
    // 删除角色
    systemRoleMapper.deleteByIds(ids);
    // 删除角色部门关联数据、角色菜单关联数据
    systemRoleDeptService.batchDeleteRoleDept(ids);
    systemRoleMenuService.batchDeleteRoleMenu(ids);
  }

  /**
   * 查询全部角色 -> TestPassed
   */
  public List<SystemRoleTb> listAllRole() {
    LambdaQueryWrapper<SystemRoleTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.orderByDesc(SystemRoleTb::getCreateTime);
    return systemRoleMapper.selectList(wrapper);
  }

  /**
   * 根据条件查询全部角色 -> TestPassed
   *
   * @param args     条件
   * @param rolePage 分页参数
   * @return /
   */
  public List<SystemRoleTb> queryRoleByArgs(SystemQueryRoleArgs args, Page<SystemRoleTb> rolePage) {
    LambdaQueryWrapper<SystemRoleTb> wrapper = new LambdaQueryWrapper<>();
    this.injectQueryParams(args, wrapper);
    return systemRoleMapper.selectList(wrapper);
  }

  private void injectQueryParams(SystemQueryRoleArgs args, LambdaQueryWrapper<SystemRoleTb> wrapper) {
    if (args != null) {
      wrapper.and(StrUtil.isNotBlank(args.getBlurry()), c -> c.like(SystemRoleTb::getName, args.getBlurry()).or()
          .like(SystemRoleTb::getDescription, args.getBlurry()));
      if (CollUtil.isNotEmpty(args.getCreateTime()) && args.getCreateTime().size() >= 2) {
        wrapper.between(SystemRoleTb::getCreateTime, args.getCreateTime().get(0), args.getCreateTime().get(1));
      }
    }
    wrapper.orderByAsc(SystemRoleTb::getLevel);
  }

  /**
   * 分页查询角色 -> TestPassed
   *
   * @param args 条件
   * @param page 分页条件
   * @return /
   */
  public KitPageResult<SystemRoleVo> searchRoleByArgs(SystemQueryRoleArgs args, Page<SystemRoleTb> page) {
    LambdaQueryWrapper<SystemRoleTb> wrapper = new LambdaQueryWrapper<>();
    this.injectQueryParams(args, wrapper);
    IPage<SystemRoleVo> data = systemRoleMapper.selectPage(page, wrapper).convert(systemUserRoleService::convertToRoleVo);
    return KitPageUtil.toPage(data);
  }

  /**
   * 查询用户权限信息 -> TestPassed?
   *
   * @param user 用户信息
   * @return 权限信息
   */
  public List<SystemRoleCodeVo> buildUserRolePermissions(SystemUserVo user) {
    Set<String> permissions = new HashSet<>();
    // 如果是管理员直接返回
    if (user.getIsAdmin()) {
      permissions.add("admin");
      return permissions.stream().map(SystemRoleCodeVo::new).collect(Collectors.toList());
    }
//    List<SystemRoleVo> roles = systemUserRoleService.listRoleVoByUsersId(user.getId());
//    permissions = roles.stream().flatMap(role -> role.getMenus().stream()).map(SystemMenuVo::getPermission)
//        .filter(StrUtil::isNotBlank).collect(Collectors.toSet());
//    return permissions.stream().map(SystemRoleCodeVo::new).collect(Collectors.toList());
    Set<Long> roleIds = systemUserRoleService.listUserRoleIdByUserId(user.getId());
    // 查询角色权限
    if (CollUtil.isNotEmpty(roleIds)) {
      return systemRoleMenuService.listMenuPermissionByRoleIds(roleIds).stream().map(SystemRoleCodeVo::new).collect(Collectors.toList());
    }
    // 没有包含任何角色，赋予默认权限
    permissions.add("IsAJoker");
    return permissions.stream().map(SystemRoleCodeVo::new).collect(Collectors.toList());
  }

  /**
   * 验证是否被用户关联 -> TestPassed
   *
   * @param ids /
   */
  public void verifyBindRelationByIds(Set<Long> ids) {
    if (systemUserRoleService.existUserWithRoleIds(ids)) {
      throw new BadRequestException("所选角色存在用户关联, 请解除关联再试！");
    }
  }

  public SystemRoleVo getRoleVoById(Long id) {
    SystemRoleTb roleTb = systemRoleMapper.selectById(id);
    if (roleTb == null) {
      return null;
    }
    return systemUserRoleService.convertToRoleVo(roleTb);
  }

  public Dict getCurrentUserRoleLevel() {
    return Dict.create().set("level", checkRoleLevels(null));
  }

  /**
   * 检查用户的角色级别 -> TestPassed
   *
   * @return /
   */
  private int checkRoleLevels(Integer level) {
    Set<Integer> levels = systemUserRoleService.listUserRoleLevelByUserId(KitSecurityHelper.getCurrentUserId());
    int min = Collections.min(levels);
    if (level != null) {
      if (level < min) {
        throw new BadRequestException("权限不足, 你的角色级别：" + min + ", 低于操作的角色级别：" + level);
      }
    }
    return min;
  }

  public void exportRoleXlsx(HttpServletResponse response, SystemQueryRoleArgs args) {
    List<SystemRoleTb> systemRoleVos = this.queryRoleByArgs(args, null);
    List<SystemRoleExportRowVo> rowVos = new ArrayList<>();
    for (SystemRoleTb dataObject : systemRoleVos) {
      SystemRoleExportRowVo rowVo = new SystemRoleExportRowVo();
      rowVo.setName(dataObject.getName());
      rowVo.setLevel(dataObject.getLevel());
      rowVo.setDescription(dataObject.getDescription());
      rowVo.setCreateTime(dataObject.getCreateTime());
      rowVos.add(rowVo);
    }
    KitExcelExporter.exportSimple(response, "部门数据", SystemRoleExportRowVo.class, rowVos);
  }
}
