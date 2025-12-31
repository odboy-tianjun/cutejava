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
import cn.odboy.system.dal.model.SystemCreateRoleArgs;
import cn.odboy.system.dal.model.SystemQueryRoleArgs;
import cn.odboy.system.dal.model.SystemRoleExportRowVo;
import cn.odboy.system.dal.model.SystemRoleVo;
import cn.odboy.system.service.SystemRoleService;
import cn.odboy.util.xlsx.KitExcelExporter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.ArrayList;
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
  @PostMapping(value = "/queryRoleById")
  @PreAuthorize("@el.check('roles:list')")
  public ResponseEntity<SystemRoleVo> queryRoleById(@RequestBody SystemRoleTb args) {
    return ResponseEntity.ok(systemRoleService.getRoleById(args.getId()));
  }

  @ApiOperation("导出角色数据")
  @GetMapping(value = "/download")
  @PreAuthorize("@el.check('role:list')")
  public void exportRole(HttpServletResponse response, SystemQueryRoleArgs args) throws IOException {
    List<SystemRoleVo> systemRoleVos = systemRoleService.queryRoleByArgs(args);
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

  @ApiOperation("返回全部的角色")
  @PostMapping(value = "/queryRoleList")
  @PreAuthorize("@el.check('roles:list','user:add','user:edit')")
  public ResponseEntity<List<SystemRoleTb>> queryRoleList() {
    return ResponseEntity.ok(systemRoleService.listAllRole());
  }

  @ApiOperation("查询角色")
  @PostMapping
  @PreAuthorize("@el.check('roles:list')")
  public ResponseEntity<KitPageResult<SystemRoleVo>> queryRoleByArgs(
      @Validated @RequestBody KitPageArgs<SystemQueryRoleArgs> pageArgs) {
    Page<Object> page = new Page<>(pageArgs.getPage(), pageArgs.getSize());
    return ResponseEntity.ok(systemRoleService.searchRoleByArgs(pageArgs.getArgs(), page));
  }

  @ApiOperation("获取用户级别")
  @PostMapping(value = "/queryRoleLevel")
  public ResponseEntity<Dict> queryRoleLevel() {
    return ResponseEntity.ok(systemRoleService.getRoleLevel());
  }

  @ApiOperation("新增角色")
  @PostMapping(value = "/saveRole")
  @PreAuthorize("@el.check('roles:add')")
  public ResponseEntity<Void> saveRole(@Validated @RequestBody SystemCreateRoleArgs args) {
    systemRoleService.saveRole(args);
    return ResponseEntity.ok(null);
  }

  @ApiOperation("修改角色")
  @PostMapping(value = "/modifyRoleById")
  @PreAuthorize("@el.check('roles:edit')")
  public ResponseEntity<Void> modifyRoleById(@Validated(SystemRoleTb.Update.class) @RequestBody SystemRoleVo args) {
    systemRoleService.updateRoleById(args);
    return ResponseEntity.ok(null);
  }

  @ApiOperation("修改角色菜单")
  @PostMapping(value = "/modifyBindMenuById")
  @PreAuthorize("@el.check('roles:edit')")
  public ResponseEntity<Void> modifyBindMenuById(@RequestBody SystemRoleVo args) {
    systemRoleService.updateBindMenuById(args);
    return ResponseEntity.ok(null);
  }

  @ApiOperation("删除角色")
  @PostMapping(value = "/removeRoleByIds")
  @PreAuthorize("@el.check('roles:del')")
  public ResponseEntity<Void> removeRoleByIds(@RequestBody Set<Long> ids) {
    systemRoleService.deleteRoleByIds(ids);
    return ResponseEntity.ok(null);
  }
}
