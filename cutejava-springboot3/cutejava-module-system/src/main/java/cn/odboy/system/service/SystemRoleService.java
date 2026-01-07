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
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.odboy.base.KitPageResult;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.system.dal.dataobject.SystemMenuTb;
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import cn.odboy.system.dal.dataobject.SystemUserTb;
import cn.odboy.system.dal.model.SystemCreateRoleArgs;
import cn.odboy.system.dal.model.SystemMenuVo;
import cn.odboy.system.dal.model.SystemQueryRoleArgs;
import cn.odboy.system.dal.model.SystemRoleCodeVo;
import cn.odboy.system.dal.model.SystemRoleExportRowVo;
import cn.odboy.system.dal.model.SystemRoleVo;
import cn.odboy.system.dal.mysql.SystemRoleMapper;
import cn.odboy.system.framework.permission.core.KitSecurityHelper;
import cn.odboy.util.KitBeanUtil;
import cn.odboy.util.KitPageUtil;
import cn.odboy.util.KitValidUtil;
import cn.odboy.util.xlsx.KitExcelExporter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
   * 创建
   *
   * @param args /
   */
  @Transactional(rollbackFor = Exception.class)
  public void saveRole(SystemCreateRoleArgs args) {
    checkRoleLevels(args.getLevel());
    if (this.getRoleByName(args.getName()) != null) {
      throw new BadRequestException("角色名称已存在");
    }
    SystemRoleTb roleTb = KitBeanUtil.copyToClass(args, SystemRoleTb.class);
    systemRoleMapper.insert(roleTb);
    // 判断是否有部门数据, 若有, 则需创建关联
    if (CollectionUtil.isNotEmpty(args.getDepts())) {
      systemRoleDeptService.batchInsertRoleDept(args.getDepts(), args.getId());
    }
  }

  private SystemRoleTb getRoleByName(String name) {
    LambdaQueryWrapper<SystemRoleTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemRoleTb::getName, name);
    return systemRoleMapper.selectOne(wrapper);
  }

  /**
   * 修改
   *
   * @param args /
   */
  @Transactional(rollbackFor = Exception.class)
  public void updateRoleById(SystemRoleVo args) {
    checkRoleLevels(args.getLevel());
    SystemRoleVo role = this.getRoleVoById(args.getId());
    SystemRoleTb role1 = this.getRoleByName(args.getName());
    if (role1 != null && !role1.getId().equals(role.getId())) {
      throw new BadRequestException("角色名称已存在");
    }
    role.setName(args.getName());
    role.setDescription(args.getDescription());
    role.setDataScope(args.getDataScope());
    role.setDepts(args.getDepts());
    role.setLevel(args.getLevel());
    // 更新
    systemRoleMapper.insertOrUpdate(role);
    // 删除关联部门数据
    systemRoleDeptService.batchDeleteRoleDept(Collections.singleton(args.getId()));
    // 判断是否有部门数据, 若有, 则需更新关联
    if (CollectionUtil.isNotEmpty(args.getDepts())) {
      systemRoleDeptService.batchInsertRoleDept(args.getDepts(), args.getId());
    }
  }

  private SystemRoleVo getRoleVoById(Long id) {
    return KitBeanUtil.copyToClass(systemRoleMapper.selectById(id), SystemRoleVo.class);
  }

  /**
   * 修改绑定的菜单
   *
   * @param args /
   */
  public void updateBindMenuById(SystemRoleVo args) {
    SystemRoleTb role = systemRoleMapper.selectById(args.getId());
    checkRoleLevels(role.getLevel());
    // 更新菜单
    systemRoleMenuService.deleteRoleMenuByRoleId(role.getId());
    // 判断是否为空
    Set<SystemMenuVo> menus = args.getMenus();
    if (CollUtil.isNotEmpty(menus)) {
      systemRoleMenuService.batchInsertRoleMenu(menus, role.getId());
    }
  }

  /**
   * 删除
   *
   * @param ids /
   */
  @Transactional(rollbackFor = Exception.class)
  public void deleteRoleByIds(Set<Long> ids) {
    List<Integer> roleLevels = systemRoleMapper.selectByIds(ids).stream().map(SystemRoleTb::getLevel).distinct().collect(Collectors.toList());
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
   * 查询全部角色
   */
  public List<SystemRoleTb> listAllRole() {
    LambdaQueryWrapper<SystemRoleTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.orderByAsc(SystemRoleTb::getLevel);
    return systemRoleMapper.selectList(wrapper);
  }

  /**
   * 根据条件查询全部角色
   *
   * @param args 条件
   * @return /
   */
  public List<SystemRoleVo> queryRoleByArgs(SystemQueryRoleArgs args) {
    KitValidUtil.notNull(args);
    // 查询角色基本信息
    LambdaQueryWrapper<SystemRoleTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.and(StrUtil.isNotBlank(args.getBlurry()),
        c -> c.like(SystemRoleTb::getName, args.getBlurry()).or()
            .like(SystemRoleTb::getDescription, args.getBlurry()));
    if (CollUtil.isNotEmpty(args.getCreateTime()) && args.getCreateTime().size() >= 2) {
      wrapper.between(SystemRoleTb::getCreateTime, args.getCreateTime().get(0),
          args.getCreateTime().get(1));
    }
    wrapper.orderByAsc(SystemRoleTb::getLevel);
    if (args.getSize() != null) {
      Page<SystemRoleTb> rolePage = new Page<>(args.getPage(), args.getSize());
      Page<SystemRoleTb> page = systemRoleMapper.selectPage(rolePage, wrapper);
      List<SystemRoleTb> roles = page.getRecords();
      return roles.stream().map(systemUserRoleService::convertToRoleVo).collect(Collectors.toList());
    } else {
      List<SystemRoleTb> roles = systemRoleMapper.selectList(wrapper);
      return roles.stream().map(systemUserRoleService::convertToRoleVo).collect(Collectors.toList());
    }
  }

  /**
   * 分页查询角色
   *
   * @param args 条件
   * @param page 分页参数
   * @return /
   */
  public KitPageResult<SystemRoleVo> searchRoleByArgs(SystemQueryRoleArgs args, Page<Object> page) {
    args.setOffset(page.offset());
    List<SystemRoleVo> roles = this.queryRoleByArgs(args);
    Long total = this.countRoleByArgs(args);
    return KitPageUtil.toPage(roles, total);
  }


  /**
   * 获取用户权限信息
   *
   * @param user 用户信息
   * @return 权限信息
   */
  public List<SystemRoleCodeVo> buildUserRolePermissions(SystemUserTb user) {
    Set<String> permissions = new HashSet<>();
    // 如果是管理员直接返回
    if (user.getIsAdmin()) {
      permissions.add("admin");
      return permissions.stream().map(SystemRoleCodeVo::new).collect(Collectors.toList());
    }
    List<SystemRoleVo> roles = systemUserRoleService.queryRoleByUsersId(user.getId());
    permissions = roles.stream().flatMap(role -> role.getMenus().stream()).map(SystemMenuTb::getPermission)
        .filter(StrUtil::isNotBlank).collect(Collectors.toSet());
    return permissions.stream().map(SystemRoleCodeVo::new).collect(Collectors.toList());
  }

  /**
   * 验证是否被用户关联
   *
   * @param ids /
   */
  public void verifyBindRelationByIds(Set<Long> ids) {
    if (systemUserRoleService.countUserByRoleIds(ids) > 0) {
      throw new BadRequestException("所选角色存在用户关联, 请解除关联再试！");
    }
  }

  public Long countRoleByArgs(SystemQueryRoleArgs args) {
    KitValidUtil.notNull(args);
    LambdaQueryWrapper<SystemRoleTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.and(StrUtil.isNotBlank(args.getBlurry()),
        c -> c.like(SystemRoleTb::getName, args.getBlurry()).or()
            .like(SystemRoleTb::getDescription, args.getBlurry()));
    if (CollUtil.isNotEmpty(args.getCreateTime()) && args.getCreateTime().size() >= 2) {
      wrapper.between(SystemRoleTb::getCreateTime, args.getCreateTime().get(0),
          args.getCreateTime().get(1));
    }
    return systemRoleMapper.selectCount(wrapper);
  }

  public SystemRoleVo getRoleById(Long id) {
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
   * 检查用户的角色级别
   *
   * @return /
   */
  private int checkRoleLevels(Integer level) {
    List<Integer> levels = systemUserRoleService.queryRoleByUsersId(KitSecurityHelper.getCurrentUserId()).stream()
        .map(SystemRoleTb::getLevel).collect(Collectors.toList());
    int min = Collections.min(levels);
    if (level != null) {
      if (level < min) {
        throw new BadRequestException("权限不足, 你的角色级别：" + min + ", 低于操作的角色级别：" + level);
      }
    }
    return min;
  }

  public void exportRoleXlsx(HttpServletResponse response, SystemQueryRoleArgs args) {
    List<SystemRoleVo> systemRoleVos = this.queryRoleByArgs(args);
//    KitXlsxExportUtil.exportFile(response, "角色数据", systemRoleVos, SystemRoleExportRowVo.class, (dataObject) -> {
//      SystemRoleExportRowVo rowVo = new SystemRoleExportRowVo();
//      rowVo.setName(dataObject.getName());
//      rowVo.setLevel(dataObject.getLevel());
//      rowVo.setDescription(dataObject.getDescription());
//      rowVo.setCreateTime(dataObject.getCreateTime());
//      return CollUtil.newArrayList(rowVo);
//    });
    List<SystemRoleExportRowVo> rowVos = new ArrayList<>();
    for (SystemRoleVo dataObject : systemRoleVos) {
      SystemRoleExportRowVo rowVo = new SystemRoleExportRowVo();
      rowVo.setName(dataObject.getName());
      rowVo.setLevel(dataObject.getLevel());
      rowVo.setDescription(dataObject.getDescription());
      rowVo.setCreateTime(dataObject.getCreateTime());
      rowVos.add(rowVo);
    }
    KitExcelExporter.exportSimple(response, "部门数据", SystemRoleExportRowVo.class, rowVos);
  }

  public List<SystemRoleTb> listByIds(Set<Long> roleIds) {
    return systemRoleMapper.selectByIds(roleIds);
  }
}
