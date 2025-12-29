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
package cn.odboy.system.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.odboy.base.KitPageArgs;
import cn.odboy.base.KitPageResult;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.system.dal.dataobject.SystemMenuTb;
import cn.odboy.system.dal.model.SystemMenuVo;
import cn.odboy.system.dal.model.SystemQueryMenuArgs;
import cn.odboy.system.dal.mysql.SystemMenuMapper;
import cn.odboy.system.framework.permission.core.KitSecurityHelper;
import cn.odboy.system.service.SystemMenuService;
import cn.odboy.util.KitPageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "系统：菜单管理")
@RequestMapping("/api/menu")
public class SystemMenuController {

  @Autowired
  private SystemMenuService systemMenuService;
  @Autowired
  private SystemMenuMapper systemMenuMapper;

  @ApiOperation("导出菜单数据")
  @GetMapping(value = "/download")
  @PreAuthorize("@el.check('menu:list')")
  public void exportMenu(HttpServletResponse response, SystemQueryMenuArgs criteria) throws Exception {
    systemMenuService.exportMenuExcel(systemMenuService.queryAllMenu(criteria, false), response);
  }

  @PostMapping(value = "/buildMenus")
  @ApiOperation("获取前端所需菜单")
  public ResponseEntity<List<SystemMenuVo>> buildMenus() {
    List<SystemMenuTb> menuList = systemMenuService.queryMenuListByUserId(KitSecurityHelper.getCurrentUserId());
    List<SystemMenuTb> menus = systemMenuService.buildMenuTree(menuList);
    return ResponseEntity.ok(systemMenuService.buildMenuVo(menus));
  }

  @ApiOperation("返回全部的菜单")
  @PostMapping(value = "/queryMenuListByPid")
  @PreAuthorize("@el.check('menu:list','roles:list')")
  public ResponseEntity<List<SystemMenuTb>> queryMenuListByPid(@RequestParam Long pid) {
    return ResponseEntity.ok(systemMenuService.queryMenuByPid(pid));
  }

  @ApiOperation("根据菜单ID返回所有子节点ID, 包含自身ID")
  @PostMapping(value = "/queryChildMenuSet")
  @PreAuthorize("@el.check('menu:list','roles:list')")
  public ResponseEntity<Set<Long>> queryChildMenuSet(@RequestParam Long id) {
    Set<SystemMenuTb> menuSet = new HashSet<>();
    List<SystemMenuTb> menuList = systemMenuService.queryMenuByPid(id);
    menuSet.add(systemMenuMapper.selectById(id));
    menuSet = systemMenuService.queryChildMenu(menuList, menuSet);
    Set<Long> ids = menuSet.stream().map(SystemMenuTb::getId).collect(Collectors.toSet());
    return ResponseEntity.ok(ids);
  }

  @PostMapping
  @ApiOperation("查询菜单")
  @PreAuthorize("@el.check('menu:list')")
  public ResponseEntity<KitPageResult<SystemMenuTb>> queryAllMenuByCrud(
      @Validated @RequestBody KitPageArgs<SystemQueryMenuArgs> args) throws Exception {
    return queryAllMenu(args);
  }

  @PostMapping(value = "/queryMenuByArgs")
  @ApiOperation("查询菜单")
  @PreAuthorize("@el.check('menu:list')")
  public ResponseEntity<KitPageResult<SystemMenuTb>> queryAllMenu(
      @Validated @RequestBody KitPageArgs<SystemQueryMenuArgs> args) throws Exception {
    SystemQueryMenuArgs criteria = args.getArgs();
    List<SystemMenuTb> menuList = systemMenuService.queryAllMenu(criteria, true);
    return ResponseEntity.ok(KitPageUtil.toPage(menuList));
  }

  @ApiOperation("查询菜单:根据ID获取同级与上级数据")
  @PostMapping("/queryMenuSuperior")
  @PreAuthorize("@el.check('menu:list')")
  public ResponseEntity<List<SystemMenuTb>> queryMenuSuperior(@RequestBody List<Long> ids) {
    Set<SystemMenuTb> menus;
    if (CollectionUtil.isNotEmpty(ids)) {
      menus = new LinkedHashSet<>(systemMenuService.queryMenuByIds(ids));
      for (SystemMenuTb menu : menus) {
        List<SystemMenuTb> menuList = systemMenuService.querySuperiorMenuList(menu, new ArrayList<>());
        for (SystemMenuTb data : menuList) {
          if (data.getId().equals(menu.getPid())) {
            data.setSubCount(data.getSubCount() - 1);
          }
        }
        menus.addAll(menuList);
      }
      // 编辑菜单时不显示自己以及自己下级的数据, 避免出现PID数据环形问题
      menus = menus.stream().filter(i -> !ids.contains(i.getId())).collect(Collectors.toSet());
      return ResponseEntity.ok(systemMenuService.buildMenuTree(new ArrayList<>(menus)));
    }
    return ResponseEntity.ok(systemMenuService.queryMenuByPid(null));
  }

  @ApiOperation("新增菜单")
  @PostMapping(value = "/saveMenu")
  @PreAuthorize("@el.check('menu:add')")
  public ResponseEntity<Void> saveMenu(@Validated @RequestBody SystemMenuTb args) {
    if (args.getId() != null) {
      throw new BadRequestException("无效参数id");
    }
    systemMenuService.saveMenu(args);
    return ResponseEntity.ok(null);
  }

  @ApiOperation("修改菜单")
  @PostMapping(value = "/modifyMenuById")
  @PreAuthorize("@el.check('menu:edit')")
  public ResponseEntity<Void> modifyMenuById(@Validated(SystemMenuTb.Update.class) @RequestBody SystemMenuTb args) {
    systemMenuService.modifyMenuById(args);
    return ResponseEntity.ok(null);
  }

  @ApiOperation("删除菜单")
  @PostMapping(value = "/removeMenuByIds")
  @PreAuthorize("@el.check('menu:del')")
  public ResponseEntity<Void> removeMenuByIds(@RequestBody Set<Long> ids) {
    systemMenuService.removeMenuByIds(ids);
    return ResponseEntity.ok(null);
  }
}
