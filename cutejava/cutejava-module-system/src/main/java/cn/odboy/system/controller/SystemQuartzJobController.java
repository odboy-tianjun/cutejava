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
import cn.odboy.framework.context.KitSpringBeanHolder;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.system.dal.dataobject.SystemQuartzJobTb;
import cn.odboy.system.dal.dataobject.SystemQuartzLogTb;
import cn.odboy.system.dal.model.SystemQueryQuartzJobArgs;
import cn.odboy.system.dal.model.SystemUpdateQuartzJobArgs;
import cn.odboy.system.service.SystemQuartzJobService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/quartzJob")
@Api(tags = "系统:定时任务管理")
public class SystemQuartzJobController {
    @Autowired private SystemQuartzJobService systemQuartzJobService;

    @ApiOperation("查询定时任务")
    @PostMapping
    @PreAuthorize("@el.check('quartzJob:list')")
    public ResponseEntity<KitPageResult<SystemQuartzJobTb>> queryQuartzJobByCrud(
        @Validated @RequestBody KitPageArgs<SystemQueryQuartzJobArgs> args) {
        SystemQueryQuartzJobArgs criteria = args.getArgs();
        Page<SystemQuartzJobTb> page = new Page<>(criteria.getPage(), criteria.getSize());
        return ResponseEntity.ok(systemQuartzJobService.queryQuartzJobByArgs(criteria, page));
    }

    @ApiOperation("导出任务数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('quartzJob:list')")
    public void exportQuartzJob(HttpServletResponse response, SystemQueryQuartzJobArgs criteria) throws IOException {
        systemQuartzJobService.exportQuartzJobExcel(systemQuartzJobService.queryQuartzJobByArgs(criteria), response);
    }

    @ApiOperation("导出日志数据")
    @GetMapping(value = "/logs/download")
    @PreAuthorize("@el.check('quartzJob:list')")
    public void exportQuartzJobLog(HttpServletResponse response, SystemQueryQuartzJobArgs criteria) throws IOException {
        systemQuartzJobService.exportQuartzLogExcel(systemQuartzJobService.queryQuartzLogByArgs(criteria), response);
    }

    @ApiOperation("查询任务执行日志")
    @PostMapping(value = "/logs")
    @PreAuthorize("@el.check('quartzJob:list')")
    public ResponseEntity<KitPageResult<SystemQuartzLogTb>> queryQuartzJobLog(SystemQueryQuartzJobArgs args) {
        Page<SystemQuartzLogTb> page = new Page<>(args.getPage(), args.getSize());
        return ResponseEntity.ok(systemQuartzJobService.queryQuartzLogByArgs(args, page));
    }

    @ApiOperation("新增定时任务")
    @PostMapping(value = "/createQuartzJob")
    @PreAuthorize("@el.check('quartzJob:add')")
    public ResponseEntity<Void> createQuartzJob(@Validated @RequestBody SystemQuartzJobTb args) {
        if (args.getId() != null) {
            throw new BadRequestException("无效参数id");
        }
        // 验证Bean是不是合法的, 合法的定时任务 Bean 需要用 @Service 定义
        checkBean(args.getBeanName());
        systemQuartzJobService.createJob(args);
        return ResponseEntity.ok(null);
    }

    @ApiOperation("修改定时任务")
    @PutMapping
    @PreAuthorize("@el.check('quartzJob:edit')")
    public ResponseEntity<Void> updateQuartzJob(
        @Validated(SystemQuartzJobTb.Update.class) @RequestBody SystemUpdateQuartzJobArgs args) {
        // 验证Bean是不是合法的, 合法的定时任务 Bean 需要用 @Service 定义
        checkBean(args.getBeanName());
        systemQuartzJobService.modifyQuartzJobResumeCron(args);
        return ResponseEntity.ok(null);
    }

    @ApiOperation("更改定时任务状态")
    @PostMapping(value = "/switchQuartzJobStatus/{id}")
    @PreAuthorize("@el.check('quartzJob:edit')")
    public ResponseEntity<Void> switchQuartzJobStatus(@PathVariable Long id) {
        systemQuartzJobService.switchQuartzJobStatus(systemQuartzJobService.getQuartzJobById(id));
        return ResponseEntity.ok(null);
    }

    @ApiOperation("执行定时任务")
    @PostMapping(value = "/startQuartzJob/{id}")
    @PreAuthorize("@el.check('quartzJob:edit')")
    public ResponseEntity<Void> startQuartzJob(@PathVariable Long id) {
        systemQuartzJobService.startQuartzJob(systemQuartzJobService.getQuartzJobById(id));
        return ResponseEntity.ok(null);
    }

    @ApiOperation("删除定时任务")
    @PostMapping(value = "/removeJobByIds")
    @PreAuthorize("@el.check('quartzJob:del')")
    public ResponseEntity<Void> removeJobByIds(@RequestBody Set<Long> ids) {
        systemQuartzJobService.removeJobByIds(ids);
        return ResponseEntity.ok(null);
    }

    /**
     * 验证Bean是不是合法的, 合法的定时任务 Bean 需要用 @Service 定义
     *
     * @param beanName Bean名称
     */
    private void checkBean(String beanName) {
        // 避免调用攻击者可以从SpringContextHolder获得控制jdbcTemplate类
        // 并使用getDeclaredMethod调用jdbcTemplate的queryForMap函数，执行任意sql命令。
        if (!KitSpringBeanHolder.getAllServiceBeanName().contains(beanName)) {
            throw new BadRequestException("非法的 Bean，请重新输入！");
        }
    }
}
