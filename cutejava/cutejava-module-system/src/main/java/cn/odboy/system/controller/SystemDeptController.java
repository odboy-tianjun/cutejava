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

import cn.odboy.base.KitPageArgs;
import cn.odboy.base.KitPageResult;
import cn.odboy.system.dal.dataobject.SystemDeptTb;
import cn.odboy.system.dal.model.SystemCreateDeptArgs;
import cn.odboy.system.dal.model.SystemQueryDeptArgs;
import cn.odboy.system.framework.operalog.OperationLog;
import cn.odboy.system.service.SystemDeptService;
import cn.odboy.util.KitPageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "系统：部门管理")
@RequestMapping("/api/dept")
public class SystemDeptController {

  @Autowired
  private SystemDeptService systemDeptService;

  @OperationLog
  @ApiOperation("导出部门数据")
  @GetMapping(value = "/download")
  @PreAuthorize("@el.check('dept:list')")
  public void exportDept(HttpServletResponse response, SystemQueryDeptArgs args) throws Exception {
    systemDeptService.exportDeptXlsx(response, args);
  }

  @ApiOperation("查询部门")
  @PostMapping(value = "/searchDept")
  @PreAuthorize("@el.check('user:list','dept:list')")
  public ResponseEntity<KitPageResult<SystemDeptTb>> queryDept(
      @Validated @RequestBody KitPageArgs<SystemQueryDeptArgs> pageArgs) throws Exception {
    List<SystemDeptTb> depts = systemDeptService.queryAllDeptByArgs(pageArgs.getArgs(), true);
    return ResponseEntity.ok(KitPageUtil.toPage(depts));
  }

  @ApiOperation("查询部门:根据ID获取同级与上级数据")
  @PostMapping("/searchDeptTree")
  @PreAuthorize("@el.check('user:list','dept:list')")
  public ResponseEntity<KitPageResult<SystemDeptTb>> queryDeptSuperiorTree(@RequestBody List<Long> ids,
      @RequestParam(defaultValue = "false") Boolean exclude) {
    return ResponseEntity.ok(systemDeptService.searchDeptTree(ids, exclude));
  }

  @OperationLog
  @ApiOperation("新增部门")
  @PostMapping(value = "/saveDept")
  @PreAuthorize("@el.check('dept:add')")
  public ResponseEntity<Void> saveDept(@Validated @RequestBody SystemCreateDeptArgs args) {
    systemDeptService.saveDept(args);
    return ResponseEntity.ok(null);
  }

  @OperationLog
  @ApiOperation("修改部门")
  @PostMapping(value = "/updateDeptById")
  @PreAuthorize("@el.check('dept:edit')")
  public ResponseEntity<Void> updateDeptById(@Validated(SystemDeptTb.Update.class) @RequestBody SystemDeptTb args) {
    systemDeptService.updateDeptById(args);
    return ResponseEntity.ok(null);
  }

  @OperationLog
  @ApiOperation("删除部门")
  @PostMapping(value = "/deleteDeptByIds")
  @PreAuthorize("@el.check('dept:del')")
  public ResponseEntity<Void> deleteDeptByIds(@RequestBody Set<Long> ids) {
    systemDeptService.deleteDeptByIds(ids);
    return ResponseEntity.ok(null);
  }
}
