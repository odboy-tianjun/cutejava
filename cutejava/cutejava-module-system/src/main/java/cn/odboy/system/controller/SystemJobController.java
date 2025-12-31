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
import cn.odboy.system.dal.dataobject.SystemJobTb;
import cn.odboy.system.dal.model.SystemCreateJobArgs;
import cn.odboy.system.dal.model.SystemJobExportRowVo;
import cn.odboy.system.dal.model.SystemQueryJobArgs;
import cn.odboy.system.service.SystemJobService;
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
@Api(tags = "系统：岗位管理")
@RequestMapping("/api/job")
public class SystemJobController {

  @Autowired
  private SystemJobService systemJobService;

  @ApiOperation("导出岗位数据")
  @GetMapping(value = "/download")
  @PreAuthorize("@el.check('job:list')")
  public void exportJob(HttpServletResponse response, SystemQueryJobArgs args) throws IOException {
    List<SystemJobTb> systemJobTbs = systemJobService.queryJobByArgs(args);
//    KitXlsxExportUtil.exportFile(response, "岗位数据", systemJobTbs, SystemJobExportRowVo.class, (dataObject) -> {
//      SystemJobExportRowVo rowVo = new SystemJobExportRowVo();
//      rowVo.setName(dataObject.getName());
//      rowVo.setEnabled(dataObject.getEnabled() ? "启用" : "停用");
//      rowVo.setCreateTime(dataObject.getCreateTime());
//      return CollUtil.newArrayList(rowVo);
//    });
    List<SystemJobExportRowVo> rowVos = new ArrayList<>();
    for (SystemJobTb dataObject : systemJobTbs) {
      SystemJobExportRowVo rowVo = new SystemJobExportRowVo();
      rowVo.setName(dataObject.getName());
      rowVo.setEnabled(dataObject.getEnabled() ? "启用" : "停用");
      rowVo.setCreateTime(dataObject.getCreateTime());
      rowVos.add(rowVo);
    }
    KitExcelExporter.exportSimple(response, "岗位数据", SystemJobExportRowVo.class, rowVos);
  }

  @ApiOperation("查询岗位")
  @PostMapping(value = "/queryAllEnableJob")
  @PreAuthorize("@el.check('job:list','user:list')")
  public ResponseEntity<KitPageResult<SystemJobTb>> queryJobByArgs(
      @Validated @RequestBody KitPageArgs<SystemQueryJobArgs> pageArgs) {
    Page<SystemJobTb> page = new Page<>(pageArgs.getPage(), pageArgs.getSize());
    return ResponseEntity.ok(systemJobService.searchJobByArgs(pageArgs.getArgs(), page));
  }

  @ApiOperation("查询岗位")
  @PostMapping
  @PreAuthorize("@el.check('job:list','user:list')")
  public ResponseEntity<KitPageResult<SystemJobTb>> queryJobByCrud(
      @Validated @RequestBody KitPageArgs<SystemQueryJobArgs> args) {
    return queryJobByArgs(args);
  }

  @ApiOperation("新增岗位")
  @PostMapping(value = "/saveJob")
  @PreAuthorize("@el.check('job:add')")
  public ResponseEntity<Void> saveJob(@Validated @RequestBody SystemCreateJobArgs args) {
    systemJobService.saveJob(args);
    return ResponseEntity.ok(null);
  }

  @ApiOperation("修改岗位")
  @PostMapping(value = "/modifyJobById")
  @PreAuthorize("@el.check('job:edit')")
  public ResponseEntity<Void> modifyJobById(@Validated(SystemJobTb.Update.class) @RequestBody SystemJobTb args) {
    systemJobService.updateJobById(args);
    return ResponseEntity.ok(null);
  }

  @ApiOperation("删除岗位")
  @PostMapping(value = "/removeJobByIds")
  @PreAuthorize("@el.check('job:del')")
  public ResponseEntity<Void> removeJobByIds(@RequestBody Set<Long> ids) {
    systemJobService.deleteJobByIds(ids);
    return ResponseEntity.ok(null);
  }
}
