package cn.odboy.core.controller.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemJobTb;
import cn.odboy.core.service.system.SystemJobService;
import cn.odboy.core.dal.model.system.CreateSystemJobArgs;
import cn.odboy.core.dal.model.system.QuerySystemJobArgs;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;


@RestController
@RequiredArgsConstructor
@Api(tags = "系统：岗位管理")
@RequestMapping("/api/job")
public class SystemJobController {
    private final SystemJobService systemJobService;

    @ApiOperation("导出岗位数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('job:list')")
    public void exportJob(HttpServletResponse response, QuerySystemJobArgs criteria) throws IOException {
        systemJobService.downloadJobExcel(systemJobService.queryJobList(criteria), response);
    }

    @ApiOperation("查询岗位")
    @GetMapping
    @PreAuthorize("@el.check('job:list','user:list')")
    public ResponseEntity<CsResultVo<List<SystemJobTb>>> queryJob(QuerySystemJobArgs criteria) {
        Page<SystemJobTb> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(systemJobService.queryJobPage(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("新增岗位")
    @PostMapping(value = "/saveJob")
    @PreAuthorize("@el.check('job:add')")
    public ResponseEntity<Void> saveJob(@Validated @RequestBody CreateSystemJobArgs resources) {
        systemJobService.saveJob(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改岗位")
    @PostMapping(value = "/modifyJobById")
    @PreAuthorize("@el.check('job:edit')")
    public ResponseEntity<Void> modifyJobById(@Validated(SystemJobTb.Update.class) @RequestBody SystemJobTb resources) {
        systemJobService.modifyJobById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除岗位")
    @PostMapping(value = "/removeJobByIds")
    @PreAuthorize("@el.check('job:del')")
    public ResponseEntity<Void> removeJobByIds(@RequestBody Set<Long> ids) {
        // 验证是否被用户关联
        systemJobService.verifyBindRelationByIds(ids);
        systemJobService.removeJobByIds(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
