package cn.odboy.core.controller.system;

import cn.odboy.base.PageResult;
import cn.odboy.core.api.system.JobApi;
import cn.odboy.core.service.system.dto.CreateJobRequest;
import cn.odboy.core.service.system.dto.QueryJobRequest;
import cn.odboy.core.dal.dataobject.system.Job;
import cn.odboy.core.service.system.JobService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    private final JobApi jobApi;
    private final JobService jobService;

    @ApiOperation("导出岗位数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('job:list')")
    public void exportJob(HttpServletResponse response, QueryJobRequest criteria) throws IOException {
        jobService.downloadJobExcel(jobApi.describeJobList(criteria), response);
    }

    @ApiOperation("查询岗位")
    @GetMapping
    @PreAuthorize("@el.check('job:list','user:list')")
    public ResponseEntity<PageResult<Job>> queryJob(QueryJobRequest criteria) {
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(jobApi.describeJobPage(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("新增岗位")
    @PostMapping(value = "/saveJob")
    @PreAuthorize("@el.check('job:add')")
    public ResponseEntity<Object> saveJob(@Validated @RequestBody CreateJobRequest resources) {
        jobService.saveJob(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改岗位")
    @PostMapping(value = "/modifyJobById")
    @PreAuthorize("@el.check('job:edit')")
    public ResponseEntity<Object> modifyJobById(@Validated(Job.Update.class) @RequestBody Job resources) {
        jobService.modifyJobById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除岗位")
    @PostMapping(value = "/removeJobByIds")
    @PreAuthorize("@el.check('job:del')")
    public ResponseEntity<Object> removeJobByIds(@RequestBody Set<Long> ids) {
        // 验证是否被用户关联
        jobApi.verifyBindRelationByIds(ids);
        jobService.removeJobByIds(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}