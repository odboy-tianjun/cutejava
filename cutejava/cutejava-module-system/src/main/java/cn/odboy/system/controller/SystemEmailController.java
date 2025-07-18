package cn.odboy.system.controller;

import cn.odboy.system.dal.dataobject.SystemEmailConfigTb;
import cn.odboy.system.dal.model.SystemSendEmailArgs;
import cn.odboy.system.service.SystemEmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequiredArgsConstructor
@RequestMapping("api/email")
@Api(tags = "工具：邮件管理")
public class SystemEmailController {
    private final SystemEmailService systemEmailService;

    @ApiOperation("查询配置")
    @PostMapping(value = "/getLastEmailConfig")
    public ResponseEntity<SystemEmailConfigTb> getLastEmailConfig() {
        return new ResponseEntity<>(systemEmailService.getLastEmailConfig(), HttpStatus.OK);
    }

    @ApiOperation("配置邮件")
    @PostMapping(value = "/modifyEmailConfig")
    public ResponseEntity<Void> modifyEmailConfig(@Validated @RequestBody SystemEmailConfigTb emailConfig) throws Exception {
        systemEmailService.modifyEmailConfig(emailConfig);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("发送邮件")
    @PostMapping(value = "/sendEmail")
    public ResponseEntity<Void> sendEmail(@Validated @RequestBody SystemSendEmailArgs sendEmailRequest) {
        systemEmailService.sendEmail(sendEmailRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
