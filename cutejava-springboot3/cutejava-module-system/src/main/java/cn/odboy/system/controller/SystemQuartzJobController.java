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
import cn.odboy.system.dal.dataobject.SystemQuartzJobTb;
import cn.odboy.system.dal.dataobject.SystemQuartzLogTb;
import cn.odboy.system.dal.model.request.SystemQueryQuartzJobArgs;
import cn.odboy.system.dal.model.request.SystemUpdateQuartzJobArgs;
import cn.odboy.system.dal.model.response.SystemQuartzJobVo;
import cn.odboy.system.framework.operalog.OperationLog;
import cn.odboy.system.service.SystemQuartzJobService;
import cn.odboy.util.KitBeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/quartzJob")
@Api(tags = "系统:定时任务管理")
public class SystemQuartzJobController {

  @Autowired
  private SystemQuartzJobService systemQuartzJobService;

  @ApiOperation("查询定时任务")
  @PostMapping(value = "/searchQuartzJob")
  @PreAuthorize("@el.check('quartzJob:list')")
  public ResponseEntity<KitPageResult<SystemQuartzJobTb>> queryQuartzJobByCrud(@Validated @RequestBody KitPageArgs<SystemQueryQuartzJobArgs> pageArgs) {
    Page<SystemQuartzJobTb> page = new Page<>(pageArgs.getPage(), pageArgs.getSize());
    return ResponseEntity.ok(systemQuartzJobService.searchQuartzJobByArgs(pageArgs.getArgs(), page));
  }

  @OperationLog
  @ApiOperation("导出定时任务数据")
  @GetMapping(value = "/download")
  @PreAuthorize("@el.check('quartzJob:list')")
  public void exportQuartzJob(HttpServletResponse response, SystemQueryQuartzJobArgs args) {
    systemQuartzJobService.exportQuartzJobXlsx(response, args);
  }

  @OperationLog
  @ApiOperation("导出定时任务日志数据")
  @GetMapping(value = "/logs/download")
  @PreAuthorize("@el.check('quartzJob:list')")
  public void exportQuartzJobLog(HttpServletResponse response, SystemQueryQuartzJobArgs args) {
    systemQuartzJobService.exportQuartzLogXlsx(response, args);
  }

  @ApiOperation("查询定时任务执行日志")
  @PostMapping(value = "/searchQuartzLog")
  @PreAuthorize("@el.check('quartzJob:list')")
  public ResponseEntity<KitPageResult<SystemQuartzLogTb>> queryQuartzJobLog(@Validated @RequestBody KitPageArgs<SystemQueryQuartzJobArgs> pageArgs) {
    Page<SystemQuartzLogTb> page = new Page<>(pageArgs.getPage(), pageArgs.getSize());
    return ResponseEntity.ok(systemQuartzJobService.searchQuartzLogByArgs(pageArgs.getArgs(), page));
  }

  @OperationLog
  @ApiOperation("新增定时任务")
  @PostMapping(value = "/createQuartzJob")
  @PreAuthorize("@el.check('quartzJob:add')")
  public ResponseEntity<Void> createQuartzJob(@Validated @RequestBody SystemQuartzJobTb args) {
    systemQuartzJobService.createJob(args);
    return ResponseEntity.ok(null);
  }

  @OperationLog
  @ApiOperation("修改定时任务")
  @PostMapping(value = "/updateQuartzJobResumeCron")
  @PreAuthorize("@el.check('quartzJob:edit')")
  public ResponseEntity<Void> updateQuartzJob(
      @Validated(SystemQuartzJobTb.Update.class) @RequestBody SystemUpdateQuartzJobArgs args) {
    systemQuartzJobService.updateQuartzJobResumeCron(args);
    return ResponseEntity.ok(null);
  }

  @OperationLog
  @ApiOperation("更改定时任务状态")
  @PostMapping(value = "/switchQuartzJobStatus/{id}")
  @PreAuthorize("@el.check('quartzJob:edit')")
  public ResponseEntity<Void> switchQuartzJobStatus(@PathVariable Long id) {
    SystemQuartzJobTb quartzJobTb = systemQuartzJobService.getQuartzJobById(id);
    SystemQuartzJobVo quartzJobVo = KitBeanUtil.copyToClass(quartzJobTb, SystemQuartzJobVo.class);
    systemQuartzJobService.switchQuartzJobStatus(quartzJobVo);
    return ResponseEntity.ok(null);
  }

  @OperationLog
  @ApiOperation("执行定时任务")
  @PostMapping(value = "/startQuartzJob/{id}")
  @PreAuthorize("@el.check('quartzJob:edit')")
  public ResponseEntity<Void> startQuartzJob(@PathVariable Long id) {
    SystemQuartzJobTb quartzJobTb = systemQuartzJobService.getQuartzJobById(id);
    SystemQuartzJobVo quartzJobVo = KitBeanUtil.copyToClass(quartzJobTb, SystemQuartzJobVo.class);
    systemQuartzJobService.startQuartzJob(quartzJobVo);
    return ResponseEntity.ok(null);
  }

  @OperationLog
  @ApiOperation("删除定时任务")
  @PostMapping(value = "/deleteJobByIds")
  @PreAuthorize("@el.check('quartzJob:del')")
  public ResponseEntity<Void> deleteJobByIds(@RequestBody Set<Long> ids) {
    systemQuartzJobService.deleteJobByIds(ids);
    return ResponseEntity.ok(null);
  }
}
