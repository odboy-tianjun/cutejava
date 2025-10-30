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

import cn.odboy.base.CsPageArgs;
import cn.odboy.base.CsPageResult;
import cn.odboy.system.dal.dataobject.SystemJobTb;
import cn.odboy.system.dal.model.SystemCreateJobArgs;
import cn.odboy.system.dal.model.SystemQueryJobArgs;
import cn.odboy.system.service.SystemJobService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@RestController
@Api(tags = "系统：岗位管理")
@RequestMapping("/api/job")
public class SystemJobController {
    @Autowired
    private SystemJobService systemJobService;

    @ApiOperation("导出岗位数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('job:list')")
    public void exportJob(HttpServletResponse response, SystemQueryJobArgs criteria) throws IOException {
        systemJobService.exportJobExcel(systemJobService.queryJobByArgs(criteria), response);
    }

    @ApiOperation("查询岗位")
    @PostMapping(value = "/queryAllEnableJob")
    @PreAuthorize("@el.check('job:list','user:list')")
    public ResponseEntity<CsPageResult<SystemJobTb>> queryJobByArgs(@Validated @RequestBody CsPageArgs<SystemQueryJobArgs> args) {
        SystemQueryJobArgs criteria = args.getArgs();
        Page<SystemJobTb> page = new Page<>(criteria.getPage(), criteria.getSize());
        return ResponseEntity.ok(systemJobService.queryJobByArgs(criteria, page));
    }

    @ApiOperation("查询岗位")
    @PostMapping
    @PreAuthorize("@el.check('job:list','user:list')")
    public ResponseEntity<CsPageResult<SystemJobTb>> queryJobByCrud(@Validated @RequestBody CsPageArgs<SystemQueryJobArgs> args) {
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
        systemJobService.modifyJobById(args);
        return ResponseEntity.ok(null);
    }

    @ApiOperation("删除岗位")
    @PostMapping(value = "/removeJobByIds")
    @PreAuthorize("@el.check('job:del')")
    public ResponseEntity<Void> removeJobByIds(@RequestBody Set<Long> ids) {
        // 验证是否被用户关联
        systemJobService.verifyBindRelationByIds(ids);
        systemJobService.removeJobByIds(ids);
        return ResponseEntity.ok(null);
    }
}
