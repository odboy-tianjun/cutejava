package cn.odboy.application.tools.rest;

import cn.odboy.application.tools.service.EmailService;
import cn.odboy.model.tools.domain.EmailConfig;
import cn.odboy.model.tools.dto.EmailDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping
    public ResponseEntity<EmailConfig> queryEmailConfig() {
        return new ResponseEntity<>(emailService.getEmailConfig(), HttpStatus.OK);
    }

    @PutMapping
    @ApiOperation("配置邮件")
    public ResponseEntity<Object> updateEmailConfig(@Validated @RequestBody EmailConfig emailConfig) throws Exception {
        emailService.updateEmailConfigOnPassChange(emailConfig);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("发送邮件")
    public ResponseEntity<Object> sendEmail(@Validated @RequestBody EmailDto emailDto) {
        emailService.sendEmail(emailDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
