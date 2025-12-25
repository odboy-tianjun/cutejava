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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.base.KitPageResult;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.system.dal.dataobject.SystemMenuTb;
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import cn.odboy.system.dal.dataobject.SystemUserTb;
import cn.odboy.system.dal.model.SystemCreateRoleArgs;
import cn.odboy.system.dal.model.SystemQueryRoleArgs;
import cn.odboy.system.dal.model.SystemRoleCodeVo;
import cn.odboy.system.dal.model.SystemRoleVo;
import cn.odboy.system.dal.mysql.SystemRoleMapper;
import cn.odboy.system.dal.mysql.SystemUserMapper;
import cn.odboy.util.KitFileUtil;
import cn.odboy.util.KitPageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
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
  private SystemUserMapper systemUserMapper;
  @Autowired
  private SystemRoleDeptService systemRoleDeptService;

  /**
   * 创建
   *
   * @param args /
   */
  @Transactional(rollbackFor = Exception.class)
  public void saveRole(SystemCreateRoleArgs args) {
    if (this.getRoleByName(args.getName()) != null) {
      throw new BadRequestException("角色名称已存在");
    }
    systemRoleMapper.insert(BeanUtil.copyProperties(args, SystemRoleTb.class));
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
  public void modifyRoleById(SystemRoleVo args) {
    SystemRoleVo role = BeanUtil.copyProperties(systemRoleMapper.selectById(args.getId()), SystemRoleVo.class);
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

  /**
   * 修改绑定的菜单
   *
   * @param role /
   */
  public void modifyBindMenuById(SystemRoleVo role) {
    // 更新菜单
    systemRoleMenuService.deleteRoleMenuByRoleId(role.getId());
    // 判断是否为空
    if (CollUtil.isNotEmpty(role.getMenus())) {
      systemRoleMenuService.batchInsertRoleMenu(role.getMenus(), role.getId());
    }
  }

  /**
   * 删除
   *
   * @param ids /
   */
  @Transactional(rollbackFor = Exception.class)
  public void removeRoleByIds(Set<Long> ids) {
    systemRoleMapper.deleteByIds(ids);
    // 删除角色部门关联数据、角色菜单关联数据
    systemRoleDeptService.batchDeleteRoleDept(ids);
    systemRoleMenuService.batchDeleteRoleMenu(ids);
  }

  /**
   * 导出数据
   *
   * @param roles    待导出的数据
   * @param response /
   * @throws IOException
   */
  public void exportRoleExcel(List<SystemRoleVo> roles, HttpServletResponse response) throws IOException {
    List<Map<String, Object>> list = new ArrayList<>();
    for (SystemRoleVo role : roles) {
      Map<String, Object> map = new LinkedHashMap<>();
      map.put("角色名称", role.getName());
      map.put("角色级别", role.getLevel());
      map.put("描述", role.getDescription());
      map.put("创建日期", role.getCreateTime());
      list.add(map);
    }
    KitFileUtil.downloadExcel(list, response);
  }

  /**
   * 查询全部角色
   *
   * @return
   */
  public List<SystemRoleTb> queryAllRole() {
    return this.selectAllRole();
  }

  private List<SystemRoleTb> selectAllRole() {
    LambdaQueryWrapper<SystemRoleTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.orderByAsc(SystemRoleTb::getLevel);
    return systemRoleMapper.selectList(wrapper);
  }

  /**
   * 根据条件查询全部角色
   *
   * @param criteria 条件
   * @return
   */
  public List<SystemRoleVo> queryRoleByArgs(SystemQueryRoleArgs criteria) {
    return systemRoleMapper.selectRoleByArgs(criteria);
  }

  /**
   * 分页查询角色
   *
   * @param criteria 条件
   * @param page     分页参数
   * @return
   */
  public KitPageResult<SystemRoleVo> queryRoleByArgs(SystemQueryRoleArgs criteria, Page<Object> page) {
    criteria.setOffset(page.offset());
    List<SystemRoleVo> roles = systemRoleMapper.selectRoleByArgs(criteria);
    Long total = systemRoleMapper.countRoleByArgs(criteria);
    return KitPageUtil.toPage(roles, total);
  }

  /**
   * 根据用户ID查询
   *
   * @param userId 用户ID
   * @return /
   */
  public List<SystemRoleVo> queryRoleByUsersId(Long userId) {
    return systemRoleMapper.selectRoleByUserId(userId);
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
      roleSet.add(getRoleById(role.getId()));
    }
    return Collections.min(roleSet.stream().map(SystemRoleTb::getLevel).collect(Collectors.toList()));
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
    List<SystemRoleVo> roles = systemRoleMapper.selectRoleByUserId(user.getId());
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
    if (systemUserMapper.countUserByRoleIds(ids) > 0) {
      throw new BadRequestException("所选角色存在用户关联, 请解除关联再试！");
    }
  }

  /**
   * 根据ID查询
   *
   * @param id /
   * @return /
   */
  public SystemRoleTb getRoleById(Long id) {
    return systemRoleMapper.selectById(id);
  }
}
