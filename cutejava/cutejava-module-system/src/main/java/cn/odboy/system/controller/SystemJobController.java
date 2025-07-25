package cn.odboy.system.controller;

import cn.odboy.base.CsPageArgs;
import cn.odboy.base.CsResultVo;
import cn.odboy.system.dal.dataobject.SystemJobTb;
import cn.odboy.system.dal.model.SystemCreateJobArgs;
import cn.odboy.system.dal.model.SystemQueryJobArgs;
import cn.odboy.system.service.SystemJobService;
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
    public void exportJob(HttpServletResponse response, SystemQueryJobArgs criteria) throws IOException {
        systemJobService.exportJobExcel(systemJobService.queryJobByArgs(criteria), response);
    }

    @ApiOperation("查询岗位")
    @PostMapping(value = "/queryAllEnableJob")
    @PreAuthorize("@el.check('job:list','user:list')")
    public ResponseEntity<CsResultVo<List<SystemJobTb>>> queryJobByArgs(@Validated @RequestBody CsPageArgs<SystemQueryJobArgs> args) {
        SystemQueryJobArgs criteria = args.getArgs();
        Page<SystemJobTb> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(systemJobService.queryJobByArgs(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("查询岗位")
    @PostMapping
    @PreAuthorize("@el.check('job:list','user:list')")
    public ResponseEntity<CsResultVo<List<SystemJobTb>>> queryJobByCrud(@Validated @RequestBody CsPageArgs<SystemQueryJobArgs> args) {
        return queryJobByArgs(args);
    }

    @ApiOperation("新增岗位")
    @PostMapping(value = "/saveJob")
    @PreAuthorize("@el.check('job:add')")
    public ResponseEntity<Void> saveJob(@Validated @RequestBody SystemCreateJobArgs resources) {
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
