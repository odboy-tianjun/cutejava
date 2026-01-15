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

import cn.odboy.annotation.AnonymousPostMapping;
import cn.odboy.system.dal.model.request.SystemUserLoginArgs;
import cn.odboy.system.dal.model.response.SystemAuthVo;
import cn.odboy.system.service.SystemAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 授权、根据token获取用户详细信息
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@Api(tags = "系统：系统授权接口")
public class SystemAuthController {

  @Autowired
  private SystemAuthService systemAuthService;

  @ApiOperation("登录授权")
  @AnonymousPostMapping(value = "/login")
  public ResponseEntity<SystemAuthVo> login(@Validated @RequestBody SystemUserLoginArgs loginRequest, HttpServletRequest request) throws Exception {
    SystemAuthVo loginInfo = systemAuthService.doLogin(loginRequest, request);
    return ResponseEntity.ok(loginInfo);
  }

  @ApiOperation("退出登录")
  @AnonymousPostMapping(value = "/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request) {
    systemAuthService.doLogout(request);
    return ResponseEntity.ok(null);
  }
}
