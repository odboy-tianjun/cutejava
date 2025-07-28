package cn.odboy.system.controller;

import cn.odboy.system.service.SystemDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "系统：组件数据源")
@RequestMapping("/api/component")
public class SystemComponentController {
    private final SystemDeptService systemDeptService;

    @ApiOperation("查询部门下拉选择数据源")
    @PostMapping(value = "/queryDeptSelectDataSource")
    @PreAuthorize("@el.check()")
    public ResponseEntity<?> queryDeptSelectDataSource() {
        return new ResponseEntity<>(systemDeptService.queryDeptSelectDataSource(), HttpStatus.OK);
    }

    @ApiOperation("查询部门下拉选择Pro数据源")
    @PostMapping(value = "/queryDeptSelectProDataSource")
    @PreAuthorize("@el.check()")
    public ResponseEntity<?> queryDeptSelectProDataSource() {
        return new ResponseEntity<>(systemDeptService.queryDeptSelectProDataSource(), HttpStatus.OK);
    }
}
