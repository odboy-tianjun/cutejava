package cn.odboy.system.application;

import cn.odboy.system.service.SystemDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "系统组件：CuteProductLineSelect")
@RequestMapping("/api/component/CuteProductLineSelect")
public class CuteProductLineSelectApi {

  @Autowired
  private SystemDeptService systemDeptService;

  @ApiOperation("查询部门下拉选择数据源")
  @PostMapping(value = "/listMetadata")
  @PreAuthorize("@el.check()")
  public ResponseEntity<?> listMetadata() {
    return ResponseEntity.ok(systemDeptService.listDeptSelectDataSource());
  }
}
