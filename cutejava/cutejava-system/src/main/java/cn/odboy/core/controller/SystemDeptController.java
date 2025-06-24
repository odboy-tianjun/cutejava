package cn.odboy.core.controller;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.SystemDeptTb;
import cn.odboy.core.dal.model.CreateSystemDeptArgs;
import cn.odboy.core.dal.model.QuerySystemDeptArgs;
import cn.odboy.core.service.SystemDeptService;
import cn.odboy.util.CsPageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Api(tags = "系统：部门管理")
@RequestMapping("/api/dept")
public class SystemDeptController {
    private final SystemDeptService systemDeptService;

    @ApiOperation("导出部门数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('dept:list')")
    public void exportDept(HttpServletResponse response, QuerySystemDeptArgs criteria) throws Exception {
        systemDeptService.exportDeptExcel(systemDeptService.queryAllDept(criteria, false), response);
    }

    @ApiOperation("查询部门")
    @GetMapping
    @PreAuthorize("@el.check('user:list','dept:list')")
    public ResponseEntity<CsResultVo<List<SystemDeptTb>>> queryDeptPage(QuerySystemDeptArgs criteria) throws Exception {
        List<SystemDeptTb> depts = systemDeptService.queryAllDept(criteria, true);
        return new ResponseEntity<>(CsPageUtil.toPage(depts), HttpStatus.OK);
    }

    @ApiOperation("查询部门:根据ID获取同级与上级数据")
    @PostMapping("/queryDeptSuperiorTree")
    @PreAuthorize("@el.check('user:list','dept:list')")
    public ResponseEntity<CsResultVo<Set<SystemDeptTb>>> queryDeptSuperiorTree(@RequestBody List<Long> ids, @RequestParam(defaultValue = "false") Boolean exclude) {
        Set<SystemDeptTb> deptSet = new LinkedHashSet<>();
        for (Long id : ids) {
            // 同级数据
            SystemDeptTb dept = systemDeptService.queryDeptById(id);
            // 上级数据
            List<SystemDeptTb> depts = systemDeptService.querySuperiorDeptListByPid(dept, new ArrayList<>());
            if (exclude) {
                for (SystemDeptTb data : depts) {
                    if (data.getId().equals(dept.getPid())) {
                        data.setSubCount(data.getSubCount() - 1);
                    }
                }
                // 编辑部门时不显示自己以及自己下级的数据，避免出现PID数据环形问题
                depts = depts.stream().filter(i -> !ids.contains(i.getId())).collect(Collectors.toList());
            }
            deptSet.addAll(depts);
        }
        return new ResponseEntity<>(systemDeptService.buildDeptTree(new ArrayList<>(deptSet)), HttpStatus.OK);
    }

    @ApiOperation("新增部门")
    @PostMapping(value = "/saveDept")
    @PreAuthorize("@el.check('dept:add')")
    public ResponseEntity<Void> saveDept(@Validated @RequestBody CreateSystemDeptArgs resources) {
        systemDeptService.saveDept(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改部门")
    @PostMapping(value = "/modifyDept")
    @PreAuthorize("@el.check('dept:edit')")
    public ResponseEntity<Void> modifyDept(@Validated(SystemDeptTb.Update.class) @RequestBody SystemDeptTb resources) {
        systemDeptService.modifyDept(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除部门")
    @PostMapping(value = "/removeDeptByIds")
    @PreAuthorize("@el.check('dept:del')")
    public ResponseEntity<Void> removeDeptByIds(@RequestBody Set<Long> ids) {
        // 获取部门，和其所有子部门
        Set<SystemDeptTb> depts = systemDeptService.traverseDeptByIdWithPids(ids);
        // 验证是否被角色或用户关联
        systemDeptService.verifyBindRelationByIds(depts);
        systemDeptService.removeDeptByIds(depts);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
