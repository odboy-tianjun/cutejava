package cn.odboy.system.controller;

import cn.odboy.system.service.SystemMonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Api(tags = "系统-服务监控管理")
@RequestMapping("/api/monitor")
public class SystemMonitorController {
    private final SystemMonitorService systemMonitorService;

    @PostMapping(value = "/queryServerMonitorInfo")
    @ApiOperation("查询服务监控")
    @PreAuthorize("@el.check('monitor:list')")
    public ResponseEntity<Map<String, Object>> queryServerMonitorInfo() {
        return new ResponseEntity<>(systemMonitorService.queryServerMonitorInfo(), HttpStatus.OK);
    }
}
