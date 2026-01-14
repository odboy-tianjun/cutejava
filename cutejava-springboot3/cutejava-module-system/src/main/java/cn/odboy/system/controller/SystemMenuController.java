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
package cn.odboy.system.controller;

import cn.odboy.base.KitPageArgs;
import cn.odboy.base.KitPageResult;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.system.dal.dataobject.SystemMenuTb;
import cn.odboy.system.dal.model.request.SystemQueryMenuArgs;
import cn.odboy.system.dal.model.response.SystemMenuRouterVo;
import cn.odboy.system.dal.model.response.SystemMenuVo;
import cn.odboy.system.framework.operalog.OperationLog;
import cn.odboy.system.service.SystemMenuService;
import cn.odboy.util.KitPageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Set;
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

  @OperationLog
  @ApiOperation("导出菜单数据")
  @GetMapping(value = "/download")
  @PreAuthorize("@el.check('menu:list')")
  public void exportMenu(HttpServletResponse response, SystemQueryMenuArgs args) throws Exception {
    systemMenuService.exportMenuXlsx(response, args);
  }

  @PostMapping(value = "/buildMenus")
  @ApiOperation("获取前端所需菜单")
  public ResponseEntity<List<SystemMenuRouterVo>> buildMenus() {
    List<SystemMenuRouterVo> systemMenuRouterVos = systemMenuService.buildFrontMenus();
    return ResponseEntity.ok(systemMenuRouterVos);
  }

  @ApiOperation("返回全部的菜单")
  @PostMapping(value = "/listMenuByPid")
  @PreAuthorize("@el.check('menu:list','roles:list')")
  public ResponseEntity<List<SystemMenuVo>> listMenuByPid(@RequestParam Long pid) {
    List<SystemMenuVo> systemMenuTbs = systemMenuService.listMenuByPid(pid);
    return ResponseEntity.ok(systemMenuTbs);
  }

  @ApiOperation("根据菜单ID返回所有子节点ID, 包含自身ID")
  @PostMapping(value = "/listChildMenuSetByMenuId")
  @PreAuthorize("@el.check('menu:list','roles:list')")
  public ResponseEntity<Set<Long>> listChildMenuSetByMenuId(@RequestParam Long id) {
    Set<Long> ids = systemMenuService.listChildMenuSetByMenuId(id);
    return ResponseEntity.ok(ids);
  }

  @PostMapping(value = "/searchMenu")
  @ApiOperation("查询菜单")
  @PreAuthorize("@el.check('menu:list')")
  public ResponseEntity<KitPageResult<SystemMenuTb>> queryAllMenu(
      @Validated @RequestBody KitPageArgs<SystemQueryMenuArgs> pageArgs) throws Exception {
    List<SystemMenuTb> menuList = systemMenuService.queryAllMenu(pageArgs.getArgs(), true);
    return ResponseEntity.ok(KitPageUtil.toPage(menuList));
  }

  @ApiOperation("查询菜单:根据ID获取同级与上级数据")
  @PostMapping("/queryMenuSuperior")
  @PreAuthorize("@el.check('menu:list')")
  public ResponseEntity<List<SystemMenuVo>> queryMenuSuperior(@RequestBody List<Long> ids) {
    List<SystemMenuVo> systemMenuTbs = systemMenuService.listMenuSuperior(ids);
    return ResponseEntity.ok(systemMenuTbs);
  }

  @OperationLog
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

  @OperationLog
  @ApiOperation("修改菜单")
  @PostMapping(value = "/updateMenuById")
  @PreAuthorize("@el.check('menu:edit')")
  public ResponseEntity<Void> updateMenuById(@Validated(SystemMenuTb.Update.class) @RequestBody SystemMenuTb args) {
    systemMenuService.updateMenuById(args);
    return ResponseEntity.ok(null);
  }

  @OperationLog
  @ApiOperation("删除菜单")
  @PostMapping(value = "/deleteMenuByIds")
  @PreAuthorize("@el.check('menu:del')")
  public ResponseEntity<Void> deleteMenuByIds(@RequestBody Set<Long> ids) {
    systemMenuService.deleteMenuByIds(ids);
    return ResponseEntity.ok(null);
  }
}
