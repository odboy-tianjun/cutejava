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

import cn.odboy.system.constant.SystemCaptchaBizEnum;
import cn.odboy.system.dal.model.SystemCheckEmailCaptchaArgs;
import cn.odboy.system.service.SystemEmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/captcha")
@Api(tags = "系统：验证码管理")
public class SystemCaptchaController {

  @Autowired
  private SystemEmailService systemEmailService;

  @ApiOperation("重置邮箱, 发送验证码")
  @PostMapping(value = "/sendResetEmailCaptcha")
  public ResponseEntity<Void> sendResetEmailCaptcha(@RequestParam String email) {
    systemEmailService.sendCaptcha(SystemCaptchaBizEnum.EMAIL_RESET_EMAIL_CODE, email);
    return ResponseEntity.ok(null);
  }

  @ApiOperation("重置密码，发送验证码")
  @PostMapping(value = "/sendResetPasswordCaptcha")
  public ResponseEntity<Void> sendResetPasswordCaptcha(@RequestParam String email) {
    systemEmailService.sendCaptcha(SystemCaptchaBizEnum.EMAIL_RESET_PWD_CODE, email);
    return ResponseEntity.ok(null);
  }

  @ApiOperation("验证码验证")
  @PostMapping(value = "/checkEmailCaptcha")
  public ResponseEntity<Void> checkEmailCaptcha(@Validated @RequestBody SystemCheckEmailCaptchaArgs args) {
    systemEmailService.checkEmailCaptcha(args);
    return ResponseEntity.ok(null);
  }
}
