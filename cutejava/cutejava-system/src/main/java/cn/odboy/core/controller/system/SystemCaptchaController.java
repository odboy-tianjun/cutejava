package cn.odboy.core.controller.system;

import cn.odboy.core.constant.system.SystemCaptchaBizEnum;
import cn.odboy.core.service.system.SystemEmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/code")
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
    public ResponseEntity<Void> checkEmailCaptcha(@RequestParam String email, @RequestParam String code, @RequestParam String codeBi) {
        SystemCaptchaBizEnum biEnum = SystemCaptchaBizEnum.getByBizCode(codeBi);
        systemEmailService.checkEmailCaptcha(biEnum, email, code);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
