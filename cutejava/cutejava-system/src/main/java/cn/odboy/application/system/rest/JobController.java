package cn.odboy.application.system.rest;

import cn.odboy.application.system.service.JobService;
import cn.odboy.base.PageResult;
import cn.odboy.model.system.domain.Job;
import cn.odboy.model.system.request.CreateJobRequest;
import cn.odboy.model.system.request.QueryJobRequest;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;


@RestController
@RequiredArgsConstructor
@Api(tags = "系统：岗位管理")
@RequestMapping("/api/job")
public class JobController {
    private final JobService jobService;

    @ApiOperation("导出岗位数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('job:list')")
    public void exportJob(HttpServletResponse response, QueryJobRequest criteria) throws IOException {
        jobService.downloadExcel(jobService.selectJobByCriteria(criteria), response);
    }

    @ApiOperation("查询岗位")
    @GetMapping
    @PreAuthorize("@el.check('job:list','user:list')")
    public ResponseEntity<PageResult<Job>> queryJob(QueryJobRequest criteria) {
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(jobService.queryJobPage(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("新增岗位")
    @PostMapping
    @PreAuthorize("@el.check('job:add')")
    public ResponseEntity<Object> createJob(@Validated @RequestBody CreateJobRequest resources) {
        jobService.createJob(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改岗位")
    @PutMapping
    @PreAuthorize("@el.check('job:edit')")
    public ResponseEntity<Object> updateJob(@Validated(Job.Update.class) @RequestBody Job resources) {
        jobService.updateJobById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除岗位")
    @DeleteMapping
    @PreAuthorize("@el.check('job:del')")
    public ResponseEntity<Object> deleteJob(@RequestBody Set<Long> ids) {
        // 验证是否被用户关联
        jobService.verifyBindRelationByIds(ids);
        jobService.deleteJobByIds(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}