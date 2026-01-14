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
import cn.odboy.system.dal.model.request.SystemCreateJobArgs;
import cn.odboy.system.dal.model.request.SystemQueryJobArgs;
import cn.odboy.system.framework.operalog.OperationLog;
import cn.odboy.system.service.SystemJobService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Set;
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

  @OperationLog
  @ApiOperation("导出岗位数据")
  @GetMapping(value = "/download")
  @PreAuthorize("@el.check('job:list')")
  public void exportJob(HttpServletResponse response, SystemQueryJobArgs args) {
    systemJobService.exportJobXlsx(response, args);
  }

  @ApiOperation("分页查询岗位")
  @PostMapping(value = "/searchJob")
  @PreAuthorize("@el.check('job:list','user:list')")
  public ResponseEntity<KitPageResult<SystemJobTb>> queryJobByArgs(@Validated @RequestBody KitPageArgs<SystemQueryJobArgs> pageArgs) {
    Page<SystemJobTb> page = new Page<>(pageArgs.getPage(), pageArgs.getSize());
    return ResponseEntity.ok(systemJobService.searchJobByArgs(pageArgs.getArgs(), page));
  }

  @OperationLog
  @ApiOperation("新增岗位")
  @PostMapping(value = "/saveJob")
  @PreAuthorize("@el.check('job:add')")
  public ResponseEntity<Void> saveJob(@Validated @RequestBody SystemCreateJobArgs args) {
    systemJobService.saveJob(args);
    return ResponseEntity.ok(null);
  }

  @OperationLog
  @ApiOperation("修改岗位")
  @PostMapping(value = "/updateJobById")
  @PreAuthorize("@el.check('job:edit')")
  public ResponseEntity<Void> updateJobById(@Validated(SystemJobTb.Update.class) @RequestBody SystemJobTb args) {
    systemJobService.updateJobById(args);
    return ResponseEntity.ok(null);
  }

  @OperationLog
  @ApiOperation("删除岗位")
  @PostMapping(value = "/deleteJobByIds")
  @PreAuthorize("@el.check('job:del')")
  public ResponseEntity<Void> deleteJobByIds(@RequestBody Set<Long> ids) {
    systemJobService.deleteJobByIds(ids);
    return ResponseEntity.ok(null);
  }
}
