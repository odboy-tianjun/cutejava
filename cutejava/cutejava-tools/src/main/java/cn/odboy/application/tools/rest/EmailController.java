package cn.odboy.application.tools.rest;

import cn.odboy.application.tools.service.EmailService;
import cn.odboy.model.tools.domain.EmailConfig;
import cn.odboy.model.tools.request.SendEmailRequest;
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
public class EmailController {
    private final EmailService emailService;

    @ApiOperation("查询配置")
    @PostMapping(value = "/describeEmailConfig")
    public ResponseEntity<EmailConfig> describeEmailConfig() {
        return new ResponseEntity<>(emailService.describeEmailConfig(), HttpStatus.OK);
    }

    @ApiOperation("配置邮件")
    @PostMapping(value = "/modifyEmailConfig")
    public ResponseEntity<Object> modifyEmailConfig(@Validated @RequestBody EmailConfig emailConfig) throws Exception {
        emailService.modifyEmailConfigOnPassChange(emailConfig);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("发送邮件")
    @PostMapping(value = "/sendEmail")
    public ResponseEntity<Object> sendEmail(@Validated @RequestBody SendEmailRequest sendEmailRequest) {
        emailService.sendEmail(sendEmailRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
