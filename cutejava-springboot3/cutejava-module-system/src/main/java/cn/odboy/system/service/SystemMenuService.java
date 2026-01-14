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
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.system.constant.SystemTransferProtocolConst;
import cn.odboy.system.constant.SystemYesOrNoChConst;
import cn.odboy.system.dal.dataobject.SystemMenuTb;
import cn.odboy.system.dal.model.export.SystemMenuExportRowVo;
import cn.odboy.system.dal.model.request.SystemQueryMenuArgs;
import cn.odboy.system.dal.model.response.SystemMenuMetaVo;
import cn.odboy.system.dal.model.response.SystemMenuRouterVo;
import cn.odboy.system.dal.model.response.SystemMenuVo;
import cn.odboy.system.dal.model.response.SystemRoleVo;
import cn.odboy.system.dal.mysql.SystemMenuMapper;
import cn.odboy.system.framework.permission.core.KitSecurityHelper;
import cn.odboy.util.KitBeanUtil;
import cn.odboy.util.KitClassUtil;
import cn.odboy.util.KitValidUtil;
import cn.odboy.util.xlsx.KitExcelExporter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemMenuService {

  @Autowired
  private SystemMenuMapper systemMenuMapper;
  @Autowired
  private SystemRoleMenuService systemRoleMenuService;
  @Autowired
  private SystemUserRoleService systemUserRoleService;

  /**
   * 创建
   *
   * @param args /
   */
  @Transactional(rollbackFor = Exception.class)
  public void saveMenu(SystemMenuTb args) {
    if (this.getMenuByTitle(args.getTitle()) != null) {
      throw new BadRequestException("菜单标题已存在");
    }
    if (StrUtil.isNotBlank(args.getComponentName())) {
      if (this.getMenuByComponentName(args.getComponentName()) != null) {
        throw new BadRequestException("菜单组件名称已存在");
      }
    }
    if (Long.valueOf(0L).equals(args.getPid())) {
      args.setPid(null);
    }
    if (args.getIFrame()) {
      if (!(args.getPath().toLowerCase().startsWith(SystemTransferProtocolConst.PREFIX_HTTP) || args.getPath()
          .toLowerCase().startsWith(SystemTransferProtocolConst.PREFIX_HTTPS))) {
        throw new BadRequestException(SystemTransferProtocolConst.PREFIX_HTTPS_BAD_REQUEST);
      }
    }
    systemMenuMapper.insert(args);
    // 计算子节点数目
    args.setSubCount(0);
    // 更新父节点菜单数目
    this.updateMenuSubCnt(args.getPid());
  }

  /**
   * 编辑
   *
   * @param args /
   */
  @Transactional(rollbackFor = Exception.class)
  public void updateMenuById(SystemMenuTb args) {
    if (args.getId().equals(args.getPid())) {
      throw new BadRequestException("上级不能为自己");
    }
    SystemMenuTb menu = systemMenuMapper.selectById(args.getId());
    if (args.getIFrame()) {
      if (!(args.getPath().toLowerCase().startsWith(SystemTransferProtocolConst.PREFIX_HTTP) || args.getPath()
          .toLowerCase().startsWith(SystemTransferProtocolConst.PREFIX_HTTPS))) {
        throw new BadRequestException(SystemTransferProtocolConst.PREFIX_HTTPS_BAD_REQUEST);
      }
    }
    SystemMenuTb menu1 = this.getMenuByTitle(args.getTitle());
    if (menu1 != null && !menu1.getId().equals(menu.getId())) {
      throw new BadRequestException("菜单标题已存在");
    }
    if (args.getPid().equals(0L)) {
      args.setPid(null);
    }
    // 记录的父节点ID
    Long oldPid = menu.getPid();
    Long newPid = args.getPid();
    if (StrUtil.isNotBlank(args.getComponentName())) {
      menu1 = this.getMenuByComponentName(args.getComponentName());
      if (menu1 != null && !menu1.getId().equals(menu.getId())) {
        throw new BadRequestException("菜单组件名称已存在");
      }
    }
    menu.setTitle(args.getTitle());
    menu.setComponent(args.getComponent());
    menu.setPath(args.getPath());
    menu.setIcon(args.getIcon());
    menu.setIFrame(args.getIFrame());
    menu.setPid(args.getPid());
    menu.setMenuSort(args.getMenuSort());
    menu.setCache(args.getCache());
    menu.setHidden(args.getHidden());
    menu.setComponentName(args.getComponentName());
    menu.setPermission(args.getPermission());
    menu.setType(args.getType());
    // auto fill
    menu.setUpdateBy(null);
    menu.setUpdateTime(null);
    systemMenuMapper.insertOrUpdate(menu);
    // 计算父级菜单节点数目
    this.updateMenuSubCnt(oldPid);
    this.updateMenuSubCnt(newPid);
  }

  /**
   * 删除
   */
  @Transactional(rollbackFor = Exception.class)
  public void deleteMenuByIds(Set<Long> ids) {
    Set<SystemMenuVo> menuSet = new HashSet<>();
    for (Long id : ids) {
      List<SystemMenuVo> menuList = this.listMenuByPid(id);
      menuSet.add(this.getMenuVoById(id));
      menuSet = this.queryChildMenuByArgs(menuList, menuSet);
    }
    List<Long> menuIds =
        menuSet.stream().map(SystemMenuVo::getId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
    if (CollUtil.isNotEmpty(menuIds)) {
      systemRoleMenuService.deleteRoleMenuByMenuIds(menuIds);
      systemMenuMapper.deleteByIds(menuIds);
    }
    for (SystemMenuVo menu : menuSet) {
      this.updateMenuSubCnt(menu.getPid());
    }
  }

  private SystemMenuVo getMenuVoById(Long id) {
    return KitBeanUtil.copyToClass(systemMenuMapper.selectById(id), SystemMenuVo.class);
  }

  @Transactional(rollbackFor = Exception.class)
  public void updateMenuSubCnt(Long menuId) {
    if (menuId != null) {
      long count = this.countMenuByPid(menuId);
      LambdaUpdateWrapper<SystemMenuTb> wrapper = new LambdaUpdateWrapper<>();
      wrapper.eq(SystemMenuTb::getId, menuId);
      wrapper.set(SystemMenuTb::getSubCount, count);
      systemMenuMapper.update(wrapper);
    }
  }

  /**
   * 查询全部数据
   *
   * @param args    条件
   * @param isQuery /
   * @return /
   * @throws Exception /
   */
  public List<SystemMenuTb> queryAllMenu(SystemQueryMenuArgs args, Boolean isQuery) throws Exception {
    if (Boolean.TRUE.equals(isQuery)) {
      args.setPidIsNull(true);
      List<Field> fields = KitClassUtil.getAllFields(args.getClass(), new ArrayList<>());
      for (Field field : fields) {
        //设置对象的访问权限, 保证对private的属性的访问
        field.setAccessible(true);
        Object val = field.get(args);
        if ("pidIsNull".equals(field.getName())) {
          continue;
        }
        // 如果有查询条件, 则不指定pidIsNull
        if (ObjectUtil.isNotNull(val)) {
          args.setPidIsNull(null);
          break;
        }
      }
    }
    return this.queryMenuByArgs(args);
  }

  /**
   * 根据当前用户查询菜单
   *
   * @param currentUserId /
   * @return /
   */
  public List<SystemMenuVo> listMenuByUserId(Long currentUserId) {
    List<SystemRoleVo> roles = systemUserRoleService.listRoleVoByUsersId(currentUserId);
    Set<Long> roleIds = roles.stream().map(SystemRoleVo::getId).collect(Collectors.toSet());
    return new ArrayList<>(systemRoleMenuService.queryMenuByRoleIds(roleIds));
  }

  /**
   * 查询所有子节点, 包含自身ID
   *
   * @param menuList /
   * @param menuSet  /
   * @return /
   */
  public Set<SystemMenuVo> queryChildMenuByArgs(List<SystemMenuVo> menuList, Set<SystemMenuVo> menuSet) {
    for (SystemMenuVo menu : menuList) {
      menuSet.add(menu);
      List<SystemMenuVo> menus = this.listMenuByPid(menu.getId());
      if (CollUtil.isNotEmpty(menus)) {
        queryChildMenuByArgs(menus, menuSet);
      }
    }
    return menuSet;
  }

  /**
   * 懒加载菜单数据
   *
   * @param pid /
   * @return /
   */
  public List<SystemMenuVo> listMenuByPid(Long pid) {
    List<SystemMenuVo> menus;
    if (pid != null && !pid.equals(0L)) {
      LambdaQueryWrapper<SystemMenuTb> wrapper = new LambdaQueryWrapper<>();
      wrapper.eq(SystemMenuTb::getPid, pid);
      wrapper.orderByAsc(SystemMenuTb::getMenuSort);
      return KitBeanUtil.copyToList(systemMenuMapper.selectList(wrapper), SystemMenuVo.class);
    } else {
      menus = this.listRootMenu();
    }
    return menus;
  }

  /**
   * 根据ID查询同级与上级数据
   *
   * @param menu  /
   * @param menus /
   * @return /
   */
  public List<SystemMenuVo> querySuperiorMenuByArgs(SystemMenuVo menu, List<SystemMenuVo> menus) {
    if (menu.getPid() == null) {
      menus.addAll(this.listRootMenu());
      return menus;
    }
    menus.addAll(this.listMenuByPid(menu.getPid()));
    return querySuperiorMenuByArgs(this.getMenuVoById(menu.getPid()), menus);
  }

  /**
   * 构建菜单树
   *
   * @param menus 原始数据
   * @return /
   */
  public List<SystemMenuVo> buildMenuTree(List<SystemMenuVo> menus) {
    List<SystemMenuVo> trees = new ArrayList<>();
    Set<Long> ids = new HashSet<>();
    for (SystemMenuVo menu : menus) {
      if (menu.getPid() == null) {
        trees.add(menu);
      }
      for (SystemMenuVo it : menus) {
        if (menu.getId().equals(it.getPid())) {
          if (menu.getChildren() == null) {
            menu.setChildren(new ArrayList<>());
          }
          menu.getChildren().add(it);
          ids.add(it.getId());
        }
      }
    }
    if (CollUtil.isNotEmpty(trees)) {
      trees = menus.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
    }
    return trees;
  }

  /**
   * 构建菜单树
   *
   * @param menus /
   * @return /
   */
  public List<SystemMenuRouterVo> buildMenuVo(List<SystemMenuVo> menus) {
    List<SystemMenuRouterVo> list = new LinkedList<>();
    for (SystemMenuVo menu : menus) {
      if (menu != null) {
        List<SystemMenuVo> menuList = menu.getChildren();
        SystemMenuRouterVo menuVo = new SystemMenuRouterVo();
        menuVo.setName(
            ObjectUtil.isNotEmpty(menu.getComponentName()) ? menu.getComponentName() : menu.getTitle());
        // 一级目录需要加斜杠, 不然会报警告
        menuVo.setPath(menu.getPid() == null ? "/" + menu.getPath() : menu.getPath());
        menuVo.setHidden(menu.getHidden());
        // 如果不是外链
        if (!menu.getIFrame()) {
          if (menu.getPid() == null) {
            menuVo.setComponent(StrUtil.isEmpty(menu.getComponent()) ? "Layout" : menu.getComponent());
            // 如果不是一级菜单，并且菜单类型为目录，则代表是多级菜单
          } else if (menu.getType() == 0) {
            menuVo.setComponent(StrUtil.isEmpty(menu.getComponent()) ? "ParentView" : menu.getComponent());
          } else if (StrUtil.isNotBlank(menu.getComponent())) {
            menuVo.setComponent(menu.getComponent());
          }
        }
        menuVo.setMeta(new SystemMenuMetaVo(menu.getTitle(), menu.getIcon(), !menu.getCache()));
        if (CollUtil.isNotEmpty(menuList)) {
          menuVo.setAlwaysShow(true);
          menuVo.setRedirect("noredirect");
          menuVo.setChildren(buildMenuVo(menuList));
          // 处理是一级菜单并且没有子菜单的情况
        } else if (menu.getPid() == null) {
          SystemMenuRouterVo menuVo1 = getMenuVo(menu, menuVo);
          menuVo.setName(null);
          menuVo.setMeta(null);
          menuVo.setComponent("Layout");
          List<SystemMenuRouterVo> list1 = new ArrayList<>();
          list1.add(menuVo1);
          menuVo.setChildren(list1);
        }
        list.add(menuVo);
      }
    }
    return list;
  }

  /**
   * 查询 MenuResponse
   *
   * @param menu   /
   * @param menuVo /
   * @return /
   */
  private SystemMenuRouterVo getMenuVo(SystemMenuVo menu, SystemMenuRouterVo menuVo) {
    SystemMenuRouterVo menuVo1 = new SystemMenuRouterVo();
    menuVo1.setMeta(menuVo.getMeta());
    // 非外链
    if (!menu.getIFrame()) {
      menuVo1.setPath("index");
      menuVo1.setName(menuVo.getName());
      menuVo1.setComponent(menuVo.getComponent());
    } else {
      menuVo1.setPath(menu.getPath());
    }
    return menuVo1;
  }

  public List<SystemMenuVo> listMenuByIds(List<Long> ids) {
    return KitBeanUtil.copyToList(systemMenuMapper.selectByIds(ids), SystemMenuVo.class);
  }

  private SystemMenuTb getMenuByComponentName(String componentName) {
    LambdaQueryWrapper<SystemMenuTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemMenuTb::getComponentName, componentName);
    return systemMenuMapper.selectOne(wrapper);
  }

  private SystemMenuTb getMenuByTitle(String title) {
    LambdaQueryWrapper<SystemMenuTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemMenuTb::getTitle, title);
    return systemMenuMapper.selectOne(wrapper);
  }

  private Long countMenuByPid(Long pid) {
    LambdaQueryWrapper<SystemMenuTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemMenuTb::getPid, pid);
    return systemMenuMapper.selectCount(wrapper);
  }

  private List<SystemMenuTb> queryMenuByArgs(SystemQueryMenuArgs args) {
    KitValidUtil.notNull(args);
    LambdaQueryWrapper<SystemMenuTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.isNull(args.getPidIsNull() != null, SystemMenuTb::getPid);
    wrapper.eq(args.getPid() != null, SystemMenuTb::getPid, args.getPid());
    wrapper.and(StrUtil.isNotBlank(args.getBlurry()), c -> c.like(SystemMenuTb::getTitle, args.getBlurry()).or()
        .like(SystemMenuTb::getComponentName, args.getBlurry()).or()
        .like(SystemMenuTb::getPermission, args.getBlurry()));
    if (CollUtil.isNotEmpty(args.getCreateTime()) && args.getCreateTime().size() >= 2) {
      wrapper.between(SystemMenuTb::getCreateTime, args.getCreateTime().get(0), args.getCreateTime().get(1));
    }
    wrapper.orderByAsc(SystemMenuTb::getMenuSort);
    return systemMenuMapper.selectList(wrapper);
  }

  private List<SystemMenuVo> listRootMenu() {
    LambdaQueryWrapper<SystemMenuTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.isNull(SystemMenuTb::getPid);
    wrapper.orderByAsc(SystemMenuTb::getMenuSort);
    return KitBeanUtil.copyToList(systemMenuMapper.selectList(wrapper), SystemMenuVo.class);
  }

  public List<SystemMenuRouterVo> buildFrontMenus() {
    List<SystemMenuVo> originMenus = this.listMenuByUserId(KitSecurityHelper.getCurrentUserId());
    List<SystemMenuVo> targetMenus = this.buildMenuTree(originMenus);
    return this.buildMenuVo(targetMenus);
  }

  public Set<Long> listChildMenuSetByMenuId(Long id) {
    Set<SystemMenuVo> menuSet = new HashSet<>();
    List<SystemMenuVo> menuList = this.listMenuByPid(id);
    menuSet.add(this.getMenuVoById(id));
    menuSet = this.queryChildMenuByArgs(menuList, menuSet);
    return menuSet.stream().map(SystemMenuVo::getId).collect(Collectors.toSet());
  }

  public List<SystemMenuVo> listMenuSuperior(List<Long> ids) {
    Set<SystemMenuVo> menus;
    List<SystemMenuVo> systemMenuTbs;
    if (CollUtil.isNotEmpty(ids)) {
      menus = new LinkedHashSet<>(this.listMenuByIds(ids));
      for (SystemMenuVo menu : menus) {
        List<SystemMenuVo> menuList = this.querySuperiorMenuByArgs(menu, new ArrayList<>());
        for (SystemMenuVo data : menuList) {
          if (data.getId().equals(menu.getPid())) {
            data.setSubCount(data.getSubCount() - 1);
          }
        }
        menus.addAll(menuList);
      }
      // 编辑菜单时不显示自己以及自己下级的数据, 避免出现PID数据环形问题
      menus = menus.stream().filter(i -> !ids.contains(i.getId())).collect(Collectors.toSet());
      List<SystemMenuVo> newMenuList = new ArrayList<>(menus);
      systemMenuTbs = this.buildMenuTree(newMenuList);
    } else {
      systemMenuTbs = this.listMenuByPid(null);
    }
    return systemMenuTbs;
  }

  public void exportMenuXlsx(HttpServletResponse response, SystemQueryMenuArgs args) throws Exception {
    List<SystemMenuTb> systemMenuTbs = this.queryAllMenu(args, false);
    List<SystemMenuExportRowVo> rowVos = new ArrayList<>();
    for (SystemMenuTb dataObject : systemMenuTbs) {
      SystemMenuExportRowVo rowVo = new SystemMenuExportRowVo();
      rowVo.setTitle(dataObject.getTitle());
      rowVo.setType(dataObject.getType() == null ? "目录" : dataObject.getType() == 1 ? "菜单" : "按钮");
      rowVo.setPermission(dataObject.getPermission());
      rowVo.setIFrame(dataObject.getIFrame() ? SystemYesOrNoChConst.YES_STR : SystemYesOrNoChConst.NO_STR);
      rowVo.setHidden(dataObject.getHidden() ? SystemYesOrNoChConst.NO_STR : SystemYesOrNoChConst.YES_STR);
      rowVo.setCache(dataObject.getCache() ? SystemYesOrNoChConst.YES_STR : SystemYesOrNoChConst.NO_STR);
      rowVo.setCreateTime(dataObject.getCreateTime());
      rowVos.add(rowVo);
    }
    KitExcelExporter.exportSimple(response, "菜单数据", SystemMenuExportRowVo.class, rowVos);
  }
}
