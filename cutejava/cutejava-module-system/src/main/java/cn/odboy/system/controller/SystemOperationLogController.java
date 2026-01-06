package cn.odboy.system.controller;

import cn.odboy.base.KitPageArgs;
import cn.odboy.base.KitPageResult;
import cn.odboy.system.dal.dataobject.SystemOperationLogTb;
import cn.odboy.system.dal.model.SystemQueryOperationLogArgs;
import cn.odboy.system.service.SystemOperationLogService;
import cn.odboy.util.KitPageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "系统：审计日志")
@RequestMapping("/api/logs")
public class SystemOperationLogController {

  @Autowired
  private SystemOperationLogService systemOperationLogService;

  @PostMapping(value = "/searchUserLog")
  @ApiOperation("用户日志查询")
  public ResponseEntity<KitPageResult<SystemOperationLogTb>> searchUserLog(@Validated @RequestBody KitPageArgs<SystemQueryOperationLogArgs> pageArgs) {
    IPage<SystemOperationLogTb> systemOperationLogTbPage = systemOperationLogService.searchUserLog(pageArgs);
    return new ResponseEntity<>(KitPageUtil.toPage(systemOperationLogTbPage), HttpStatus.OK);
  }
}
