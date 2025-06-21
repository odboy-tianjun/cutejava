package cn.odboy.core.controller.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.context.SpringBeanHolder;
import cn.odboy.core.dal.dataobject.system.SystemQuartzJobTb;
import cn.odboy.core.dal.dataobject.system.SystemQuartzLogTb;
import cn.odboy.core.service.system.SystemQuartzJobService;
import cn.odboy.core.dal.model.system.QuerySystemQuartzJobArgs;
import cn.odboy.core.dal.model.system.UpdateSystemQuartzJobArgs;
import cn.odboy.exception.BadRequestException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs")
@Api(tags = "系统:定时任务管理")
public class SystemQuartzJobController {
    private static final String ENTITY_NAME = "quartzJob";
    private final SystemQuartzJobService systemQuartzJobService;

    @ApiOperation("查询定时任务")
    @GetMapping
    @PreAuthorize("@el.check('timing:list')")
    public ResponseEntity<CsResultVo<List<SystemQuartzJobTb>>> queryQuartzJob(QuerySystemQuartzJobArgs criteria) {
        Page<SystemQuartzJobTb> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(systemQuartzJobService.describeQuartzJobPage(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("导出任务数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('timing:list')")
    public void exportQuartzJob(HttpServletResponse response, QuerySystemQuartzJobArgs criteria) throws IOException {
        systemQuartzJobService.downloadQuartzJobExcel(systemQuartzJobService.describeQuartzJobList(criteria), response);
    }

    @ApiOperation("导出日志数据")
    @GetMapping(value = "/logs/download")
    @PreAuthorize("@el.check('timing:list')")
    public void exportQuartzJobLog(HttpServletResponse response, QuerySystemQuartzJobArgs criteria) throws IOException {
        systemQuartzJobService.downloadQuartzLogExcel(systemQuartzJobService.describeQuartzLogList(criteria), response);
    }

    @ApiOperation("查询任务执行日志")
    @GetMapping(value = "/logs")
    @PreAuthorize("@el.check('timing:list')")
    public ResponseEntity<CsResultVo<List<SystemQuartzLogTb>>> queryQuartzJobLog(QuerySystemQuartzJobArgs criteria) {
        Page<SystemQuartzLogTb> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(systemQuartzJobService.describeQuartzLogPage(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("新增定时任务")
    @PostMapping
    @PreAuthorize("@el.check('timing:add')")
    public ResponseEntity<Object> createQuartzJob(@Validated @RequestBody SystemQuartzJobTb resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        // 验证Bean是不是合法的，合法的定时任务 Bean 需要用 @Service 定义
        checkBean(resources.getBeanName());
        systemQuartzJobService.createJob(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改定时任务")
    @PutMapping
    @PreAuthorize("@el.check('timing:edit')")
    public ResponseEntity<Object> updateQuartzJob(@Validated(SystemQuartzJobTb.Update.class) @RequestBody UpdateSystemQuartzJobArgs resources) {
        // 验证Bean是不是合法的，合法的定时任务 Bean 需要用 @Service 定义
        checkBean(resources.getBeanName());
        systemQuartzJobService.modifyQuartzJobResumeCron(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("更改定时任务状态")
    @PostMapping(value = "/switchQuartzJobStatus/{id}")
    @PreAuthorize("@el.check('timing:edit')")
    public ResponseEntity<Object> switchQuartzJobStatus(@PathVariable Long id) {
        systemQuartzJobService.switchQuartzJobStatus(systemQuartzJobService.getById(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("执行定时任务")
    @PostMapping(value = "/startQuartzJob/{id}")
    @PreAuthorize("@el.check('timing:edit')")
    public ResponseEntity<Object> startQuartzJob(@PathVariable Long id) {
        systemQuartzJobService.startQuartzJob(systemQuartzJobService.getById(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除定时任务")
    @DeleteMapping
    @PreAuthorize("@el.check('timing:del')")
    public ResponseEntity<Object> removeJobByIds(@RequestBody Set<Long> ids) {
        systemQuartzJobService.removeJobByIds(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 验证Bean是不是合法的，合法的定时任务 Bean 需要用 @Service 定义
     *
     * @param beanName Bean名称
     */
    private void checkBean(String beanName) {
        // 避免调用攻击者可以从SpringContextHolder获得控制jdbcTemplate类
        // 并使用getDeclaredMethod调用jdbcTemplate的queryForMap函数，执行任意sql命令。
        if (!SpringBeanHolder.getAllServiceBeanName().contains(beanName)) {
            throw new BadRequestException("非法的 Bean，请重新输入！");
        }
    }
}
