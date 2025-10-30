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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/captcha")
@Tag(name = "系统：验证码管理")
public class SystemCaptchaController {
    @Autowired
    private SystemEmailService systemEmailService;

    @Operation(summary = "重置邮箱, 发送验证码")
    @PostMapping(value = "/sendResetEmailCaptcha")
    public ResponseEntity<Void> sendResetEmailCaptcha(@RequestParam String email) {
        systemEmailService.sendCaptcha(SystemCaptchaBizEnum.EMAIL_RESET_EMAIL_CODE, email);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "重置密码，发送验证码")
    @PostMapping(value = "/sendResetPasswordCaptcha")
    public ResponseEntity<Void> sendResetPasswordCaptcha(@RequestParam String email) {
        systemEmailService.sendCaptcha(SystemCaptchaBizEnum.EMAIL_RESET_PWD_CODE, email);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "验证码验证")
    @PostMapping(value = "/checkEmailCaptcha")
    public ResponseEntity<Void> checkEmailCaptcha(@Validated @RequestBody SystemCheckEmailCaptchaArgs args) {
        SystemCaptchaBizEnum biEnum = SystemCaptchaBizEnum.getByBizCode(args.getBizCode());
        systemEmailService.checkEmailCaptcha(biEnum, args.getEmail(), args.getCode());
        return ResponseEntity.ok(null);
    }
}
