/*
 * Copyright 2021-2025 Odboy
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
import cn.odboy.system.dal.model.SystemUserInfoVo;
import cn.odboy.system.dal.model.SystemUserLoginArgs;
import cn.odboy.system.service.SystemAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
  public ResponseEntity<Map<String, Object>> login(@Validated @RequestBody SystemUserLoginArgs loginRequest,
      HttpServletRequest request) throws Exception {
    Map<String, Object> loginInfo = systemAuthService.doLogin(loginRequest, request);
    return ResponseEntity.ok(loginInfo);
  }

  @ApiOperation("退出登录")
  @AnonymousPostMapping(value = "/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request) {
    systemAuthService.doLogout(request);
    return ResponseEntity.ok(null);
  }
}
