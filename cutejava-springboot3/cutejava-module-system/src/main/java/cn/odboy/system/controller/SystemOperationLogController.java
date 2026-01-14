/*
 * Copyright 2021-2026 Odboy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.odboy.system.controller;

import cn.odboy.base.KitPageArgs;
import cn.odboy.base.KitPageResult;
import cn.odboy.system.dal.dataobject.SystemOperationLogTb;
import cn.odboy.system.dal.model.request.SystemQueryOperationLogArgs;
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
