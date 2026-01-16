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
import cn.odboy.system.dal.mysql.SystemMenuMapper;
import cn.odboy.system.framework.permission.core.KitSecurityHelper;
import cn.odboy.util.KitBeanUtil;
import cn.odboy.util.KitClassUtil;
import cn.odboy.util.KitValidUtil;
import cn.odboy.util.xlsx.KitExcelExporter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SystemMenuService {

  @Autowired
  private SystemMenuMapper systemMenuMapper;
  @Autowired
  private SystemRoleMenuService systemRoleMenuService;
  @Autowired
  private SystemUserRoleService systemUserRoleService;
  @Lazy
  @Autowired
  private SystemMenuService proxyService;

  /**
   * 创建 -> TestPassed
   *
   * @param args /
   */
  @Transactional(rollbackFor = Exception.class)
  public void saveMenu(SystemMenuTb args) {
    if (this.existMenuWithTitle(args.getTitle())) {
      throw new BadRequestException("菜单标题已存在");
    }
    if (StrUtil.isNotBlank(args.getComponentName())) {
      if (this.existMenuWithComponentName(args.getComponentName())) {
        throw new BadRequestException("菜单组件名称已存在");
      }
    }
    if (Long.valueOf(0L).equals(args.getPid())) {
      // 顶级目录
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
   * 编辑 -> TestPassed
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
    if (this.existMenuWithTitleNeSelf(args.getTitle(), menu.getId())) {
      throw new BadRequestException("菜单标题已存在");
    }
    if (args.getPid().equals(0L)) {
      args.setPid(null);
    }
    // 记录的父节点ID
    Long oldPid = menu.getPid();
    Long newPid = args.getPid();
    if (StrUtil.isNotBlank(args.getComponentName())) {
      if (this.existMenuWithComponentNameNeSelf(args.getTitle(), menu.getId())) {
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
    systemMenuMapper.updateById(menu);
    // 计算父级菜单节点数目
    this.updateMenuSubCnt(oldPid);
    this.updateMenuSubCnt(newPid);
  }

  /**
   * 删除 -> TestPassed
   */
  @Transactional(rollbackFor = Exception.class)
  public void deleteMenuByIds(Set<Long> ids) {
    //    Set<SystemMenuVo> menuSet = new HashSet<>();
    //    for (Long id : ids) {
    //      List<SystemMenuVo> menuList = this.listMenuByPid(id);
    //      menuSet.add(this.getMenuVoById(id));
    //      menuSet = this.queryChildMenuByArgs(menuList, menuSet);
    //    }
    //    List<Long> menuIds = menuSet.stream().map(SystemMenuVo::getId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
    //    if (CollUtil.isNotEmpty(menuIds)) {
    //      systemRoleMenuService.deleteRoleMenuByMenuIds(menuIds);
    //      systemMenuMapper.deleteByIds(menuIds);
    //    }
    //    for (SystemMenuVo menu : menuSet) {
    //      this.updateMenuSubCnt(menu.getPid());
    //    }
    // ========= 优化 20260114 =========
    if (CollUtil.isEmpty(ids)) {
      return;
    }
    // 查询所有菜单数据
    List<SystemMenuTb> allMenus = systemMenuMapper.listAll();
    // 根据ID集合查询当前菜单和其子菜单
    Set<Long> allMenuIds = this.getAllMenuIdsWithChildren(ids, allMenus);
    if (CollUtil.isNotEmpty(allMenuIds)) {
      // 删除角色菜单关联
      systemRoleMenuService.deleteRoleMenuByMenuIds(allMenuIds);
      // 批量删除菜单
      systemMenuMapper.deleteByIds(allMenuIds);
      // 更新父级菜单子节点数量
      this.updateParentMenuCounts(allMenuIds, allMenus);
    }
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
  public List<SystemMenuTb> listMenuByUserId(Long currentUserId) {
//    List<SystemRoleVo> roles = systemUserRoleService.listRoleVoByUsersId(currentUserId);
//    Set<Long> roleIds = roles.stream().map(SystemRoleVo::getId).collect(Collectors.toSet());
    Set<Long> roleIds = systemUserRoleService.listUserRoleIdByUserId(currentUserId);
    return systemRoleMenuService.listMenuByRoleIds(roleIds, false);
  }

  /**
   * 懒加载菜单数据
   *
   * @param pid /
   * @return /
   */
  public List<SystemMenuTb> listMenuByPid(Long pid) {
    List<SystemMenuTb> menus;
    if (pid != null && !pid.equals(0L)) {
      LambdaQueryWrapper<SystemMenuTb> wrapper = new LambdaQueryWrapper<>();
      wrapper.eq(SystemMenuTb::getPid, pid);
      wrapper.orderByAsc(SystemMenuTb::getMenuSort);
      return systemMenuMapper.selectList(wrapper);
    } else {
      menus = this.listRootMenu();
    }
    return menus;
  }

  /**
   * 根据ID查询同级与上级数据 -> TestPassed
   *
   * @param menu  /
   * @param menus /
   * @return /
   */
  public List<SystemMenuTb> querySuperiorMenuByArgs(SystemMenuTb menu, List<SystemMenuTb> menus) {
    if (menu.getPid() == null) {
      menus.addAll(this.listRootMenu());
      return menus;
    }
    menus.addAll(this.listMenuByPid(menu.getPid()));
    return querySuperiorMenuByArgs(this.getMenuById(menu.getPid()), menus);
  }

  /**
   * 构建菜单树
   *
   * @param menus 原始数据
   * @return /
   */
  public List<SystemMenuVo> buildMenuTree(List<SystemMenuTb> menus) {
    List<SystemMenuVo> systemMenuVos = KitBeanUtil.copyToList(menus, SystemMenuVo.class);
    List<SystemMenuVo> trees = new ArrayList<>();
    Set<Long> ids = new HashSet<>();
    for (SystemMenuVo menu : systemMenuVos) {
      if (menu.getPid() == null) {
        trees.add(menu);
      }
      for (SystemMenuVo it : systemMenuVos) {
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
      trees = systemMenuVos.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
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

  public List<SystemMenuTb> listMenuByIds(List<Long> ids) {
    return systemMenuMapper.selectByIds(ids);
  }

  public List<SystemMenuRouterVo> buildFrontMenus() {
    List<SystemMenuTb> originMenus = this.listMenuByUserId(KitSecurityHelper.getCurrentUserId());
    List<SystemMenuVo> targetMenus = this.buildMenuTree(originMenus);
    return this.buildMenuVo(targetMenus);
  }

  public Set<Long> listChildMenuSetByMenuId(Long id) {
//    Set<SystemMenuTb> menuSet = new HashSet<>();
//    List<SystemMenuTb> menuList = this.listMenuByPid(id);
//    menuSet.add(this.getMenuById(id));
//    menuSet = this.queryChildMenuByArgs(menuList, menuSet);
//    return menuSet.stream().map(SystemMenuTb::getId).collect(Collectors.toSet());
    List<SystemMenuTb> allMenus = systemMenuMapper.listAll();
    return this.getAllMenuIdsWithChildren(CollUtil.newHashSet(id), allMenus);
  }

  public List<SystemMenuVo> listMenuSuperior(List<Long> ids) {
    Set<SystemMenuTb> menus;
    if (CollUtil.isNotEmpty(ids)) {
      menus = new LinkedHashSet<>(this.listMenuByIds(ids));
      for (SystemMenuTb menu : menus) {
        List<SystemMenuTb> menuList = this.querySuperiorMenuByArgs(menu, new ArrayList<>());
        for (SystemMenuTb data : menuList) {
          if (data.getId().equals(menu.getPid())) {
            data.setSubCount(data.getSubCount() - 1);
          }
        }
        menus.addAll(menuList);
      }
      // 编辑菜单时不显示自己以及自己下级的数据, 避免出现PID数据环形问题
      menus = menus.stream().filter(i -> !ids.contains(i.getId())).collect(Collectors.toSet());
      List<SystemMenuTb> newMenuList = new ArrayList<>(menus);
      return this.buildMenuTree(newMenuList);
    } else {
      List<SystemMenuTb> records = this.listMenuByPid(null);
      return KitBeanUtil.copyToList(records, SystemMenuVo.class);
    }
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

  /**
   * 查询根目录 -> TestPassed
   */
  private List<SystemMenuTb> listRootMenu() {
    LambdaQueryWrapper<SystemMenuTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.isNull(SystemMenuTb::getPid);
    wrapper.orderByAsc(SystemMenuTb::getMenuSort);
    return systemMenuMapper.selectList(wrapper);
  }

  /**
   * 根据组件名称查询菜单是否存在 -> TestPassed
   *
   * @param componentName 组件名称
   * @return /
   */
  private boolean existMenuWithComponentName(String componentName) {
    LambdaQueryWrapper<SystemMenuTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemMenuTb::getComponentName, componentName);
    return systemMenuMapper.exists(wrapper);
  }

  /**
   * 根据组件名称查询菜单是否存在 -> TestPassed
   *
   * @param componentName 组件名称
   * @return /
   */
  private boolean existMenuWithComponentNameNeSelf(String componentName, Long currentMenuId) {
    LambdaQueryWrapper<SystemMenuTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemMenuTb::getComponentName, componentName);
    wrapper.ne(SystemMenuTb::getId, currentMenuId);
    return systemMenuMapper.exists(wrapper);
  }

  /**
   * 根据菜单标题查询菜单是否存在 -> TestPassed
   *
   * @param title 菜单标题
   * @return /
   */
  private boolean existMenuWithTitle(String title) {
    LambdaQueryWrapper<SystemMenuTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemMenuTb::getTitle, title);
    return systemMenuMapper.exists(wrapper);
  }

  /**
   * 根据菜单标题查询菜单是否存在 -> TestPassed
   *
   * @param title 菜单标题
   * @return /
   */
  private boolean existMenuWithTitleNeSelf(String title, Long currentMenuId) {
    LambdaQueryWrapper<SystemMenuTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemMenuTb::getTitle, title);
    wrapper.ne(SystemMenuTb::getId, currentMenuId);
    return systemMenuMapper.exists(wrapper);
  }

  /**
   * 从所有菜单中找出指定ID及其子菜单ID
   *
   * @param rootIds  要删除的根菜单ID集合
   * @param allMenus 所有菜单数据
   * @return 包含根菜单及其所有子菜单的ID集合
   */
  private Set<Long> getAllMenuIdsWithChildren(Set<Long> rootIds, List<SystemMenuTb> allMenus) {
    Set<Long> result = new HashSet<>(rootIds);
    Set<Long> currentLevelIds = new HashSet<>(rootIds);
    // 构建父ID到子菜单的映射
    Map<Long, List<SystemMenuTb>> parentToChildrenMap = allMenus.stream().filter(menu -> menu.getPid() != null)
        .collect(Collectors.groupingBy(SystemMenuTb::getPid));
    while (!currentLevelIds.isEmpty()) {
      Set<Long> nextLevelIds = new HashSet<>();
      // 对于当前层级的每个菜单ID，查找其子菜单
      for (Long parentId : currentLevelIds) {
        List<SystemMenuTb> children = parentToChildrenMap.get(parentId);
        if (children != null && !children.isEmpty()) {
          for (SystemMenuTb child : children) {
            if (result.add(child.getId())) { // 如果成功添加（之前不存在），则继续查找其子菜单
              nextLevelIds.add(child.getId());
            }
          }
        }
      }
      currentLevelIds = nextLevelIds;
    }
    return result;
  }

  /**
   * 更新被删除菜单的父菜单子节点计数
   */
  private void updateParentMenuCounts(Set<Long> deletedMenuIds, List<SystemMenuTb> allMenus) {
    // 查询被删除菜单的父ID
    Set<Long> parentIds =
        allMenus.stream().filter(menu -> deletedMenuIds.contains(menu.getId()) && menu.getPid() != null)
            .map(SystemMenuTb::getPid).collect(Collectors.toSet());
    // 更新每个父菜单的子节点计数
    for (Long parentId : parentIds) {
      proxyService.updateMenuSubCnt(parentId);
    }
  }

  /**
   * 根据菜单id查询目录 -> TestPassed
   *
   * @param id 菜单id
   */
  private SystemMenuTb getMenuById(Long id) {
    return systemMenuMapper.selectById(id);
  }

  /**
   * 查询所有子节点, 包含自身ID
   *
   * @param menuList /
   * @param menuSet  /
   * @return /
   */
  private Set<SystemMenuTb> queryChildMenuByArgs(List<SystemMenuTb> menuList, Set<SystemMenuTb> menuSet) {
    for (SystemMenuTb menu : menuList) {
      menuSet.add(menu);
      List<SystemMenuTb> menus = this.listMenuByPid(menu.getId());
      if (CollUtil.isNotEmpty(menus)) {
        queryChildMenuByArgs(menus, menuSet);
      }
    }
    return menuSet;
  }
}
