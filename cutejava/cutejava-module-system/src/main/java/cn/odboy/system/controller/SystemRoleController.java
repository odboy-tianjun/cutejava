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

import cn.hutool.core.lang.Dict;
import cn.odboy.base.KitPageArgs;
import cn.odboy.base.KitPageResult;
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import cn.odboy.system.dal.model.request.SystemCreateRoleArgs;
import cn.odboy.system.dal.model.request.SystemQueryRoleArgs;
import cn.odboy.system.dal.model.response.SystemRoleVo;
import cn.odboy.system.framework.operalog.OperationLog;
import cn.odboy.system.service.SystemRoleService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "系统：角色管理")
@RequestMapping("/api/role")
public class SystemRoleController {

  @Autowired
  private SystemRoleService systemRoleService;

  @ApiOperation("获取单个role")
  @PostMapping(value = "/getRoleById")
  @PreAuthorize("@el.check('roles:list')")
  public ResponseEntity<SystemRoleVo> getRoleById(@RequestBody SystemRoleTb args) {
    return ResponseEntity.ok(systemRoleService.getRoleById(args.getId()));
  }

  @ApiOperation("导出角色数据")
  @GetMapping(value = "/download")
  @PreAuthorize("@el.check('role:list')")
  public void exportRole(HttpServletResponse response, SystemQueryRoleArgs args) throws IOException {
    systemRoleService.exportRoleXlsx(response, args);
  }

  @ApiOperation("返回全部的角色")
  @PostMapping(value = "/listAllRole")
  @PreAuthorize("@el.check('roles:list','user:add','user:edit')")
  public ResponseEntity<List<SystemRoleTb>> listAllRole() {
    return ResponseEntity.ok(systemRoleService.listAllRole());
  }

  @ApiOperation("查询角色")
  @PostMapping(value = "/searchRole")
  @PreAuthorize("@el.check('roles:list')")
  public ResponseEntity<KitPageResult<SystemRoleVo>> queryRoleByArgs(
      @Validated @RequestBody KitPageArgs<SystemQueryRoleArgs> pageArgs) {
    Page<Object> page = new Page<>(pageArgs.getPage(), pageArgs.getSize());
    return ResponseEntity.ok(systemRoleService.searchRoleByArgs(pageArgs.getArgs(), page));
  }

  @ApiOperation("获取用户级别")
  @PostMapping(value = "/getCurrentUserRoleLevel")
  public ResponseEntity<Dict> getCurrentUserRoleLevel() {
    return ResponseEntity.ok(systemRoleService.getCurrentUserRoleLevel());
  }

  @OperationLog
  @ApiOperation("新增角色")
  @PostMapping(value = "/saveRole")
  @PreAuthorize("@el.check('roles:add')")
  public ResponseEntity<Void> saveRole(@Validated @RequestBody SystemCreateRoleArgs args) {
    systemRoleService.saveRole(args);
    return ResponseEntity.ok(null);
  }

  @OperationLog
  @ApiOperation("修改角色")
  @PostMapping(value = "/updateRoleById")
  @PreAuthorize("@el.check('roles:edit')")
  public ResponseEntity<Void> updateRoleById(@Validated(SystemRoleTb.Update.class) @RequestBody SystemRoleVo args) {
    systemRoleService.updateRoleById(args);
    return ResponseEntity.ok(null);
  }

  @OperationLog
  @ApiOperation("修改角色菜单")
  @PostMapping(value = "/updateBindMenuById")
  @PreAuthorize("@el.check('roles:edit')")
  public ResponseEntity<Void> updateBindMenuById(@RequestBody SystemRoleVo args) {
    systemRoleService.updateBindMenuById(args);
    return ResponseEntity.ok(null);
  }

  @OperationLog
  @ApiOperation("删除角色")
  @PostMapping(value = "/deleteRoleByIds")
  @PreAuthorize("@el.check('roles:del')")
  public ResponseEntity<Void> deleteRoleByIds(@RequestBody Set<Long> ids) {
    systemRoleService.deleteRoleByIds(ids);
    return ResponseEntity.ok(null);
  }
}
