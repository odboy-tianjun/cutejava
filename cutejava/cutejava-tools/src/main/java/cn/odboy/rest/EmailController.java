package cn.odboy.rest;

import cn.odboy.domain.EmailConfig;
import cn.odboy.domain.dto.EmailDto;
import cn.odboy.service.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        return new ResponseEntity<>(emailService.find(), HttpStatus.OK);
    }

    @PutMapping
    @ApiOperation("配置邮件")
    public ResponseEntity<Object> updateEmailConfig(@Validated @RequestBody EmailConfig emailConfig) throws Exception {
        emailService.config(emailConfig, emailService.find());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("发送邮件")
    public ResponseEntity<Object> sendEmail(@Validated @RequestBody EmailDto emailDto) {
        emailService.send(emailDto, emailService.find());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
