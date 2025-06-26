package cn.odboy.system.controller;

import cn.odboy.system.constant.SystemCaptchaBizEnum;
import cn.odboy.system.dal.model.SystemCheckEmailCaptchaArgs;
import cn.odboy.system.service.SystemEmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/captcha")
@Api(tags = "系统：验证码管理")
public class SystemCaptchaController {
    private final SystemEmailService systemEmailService;

    @ApiOperation("重置邮箱，发送验证码")
    @PostMapping(value = "/sendResetEmailCaptcha")
    public ResponseEntity<Void> sendResetEmailCaptcha(@RequestParam String email) {
        systemEmailService.sendCaptcha(SystemCaptchaBizEnum.EMAIL_RESET_EMAIL_CODE, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("重置密码，发送验证码")
    @PostMapping(value = "/sendResetPasswordCaptcha")
    public ResponseEntity<Void> sendResetPasswordCaptcha(@RequestParam String email) {
        systemEmailService.sendCaptcha(SystemCaptchaBizEnum.EMAIL_RESET_PWD_CODE, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("验证码验证")
    @PostMapping(value = "/checkEmailCaptcha")
    public ResponseEntity<Void> checkEmailCaptcha(@Validated @RequestBody SystemCheckEmailCaptchaArgs args) {
        SystemCaptchaBizEnum biEnum = SystemCaptchaBizEnum.getByBizCode(args.getBizCode());
        systemEmailService.checkEmailCaptcha(biEnum, args.getEmail(), args.getCode());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
