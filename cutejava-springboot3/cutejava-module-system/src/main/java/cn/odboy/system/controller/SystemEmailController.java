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

import cn.odboy.system.dal.dataobject.SystemEmailConfigTb;
import cn.odboy.system.dal.model.SystemSendEmailArgs;
import cn.odboy.system.service.SystemEmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发送邮件
 */
@RestController
@RequestMapping("api/email")
@Tag(name = "工具：邮件管理")
public class SystemEmailController {
    @Autowired
    private SystemEmailService systemEmailService;

    @Operation(summary = "查询配置")
    @PostMapping(value = "/getLastEmailConfig")
    public ResponseEntity<SystemEmailConfigTb> getLastEmailConfig() {
        return ResponseEntity.ok(systemEmailService.getLastEmailConfig());
    }

    @Operation(summary = "配置邮件")
    @PostMapping(value = "/modifyEmailConfig")
    public ResponseEntity<Void> modifyEmailConfig(@Validated @RequestBody SystemEmailConfigTb emailConfig) throws Exception {
        systemEmailService.modifyEmailConfig(emailConfig);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "发送邮件")
    @PostMapping(value = "/sendEmail")
    public ResponseEntity<Void> sendEmail(@Validated @RequestBody SystemSendEmailArgs sendEmailRequest) {
        systemEmailService.sendEmail(sendEmailRequest);
        return ResponseEntity.ok(null);
    }
}
