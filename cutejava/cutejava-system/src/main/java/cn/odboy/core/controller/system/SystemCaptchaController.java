package cn.odboy.core.controller.system;

import cn.odboy.core.constant.system.SystemCaptchaBizEnum;
import cn.odboy.core.service.system.SystemCaptchaService;
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

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/code")
@Api(tags = "系统：验证码管理")
public class SystemCaptchaController {
    private final SystemCaptchaService captchaService;
    private final SystemEmailService emailService;

    /**
     * 除了这个其他的接口都没有用上
     *
     * @param email 邮件地址
     */
    @ApiOperation("重置邮箱，发送验证码")
    @PostMapping(value = "/sendResetEmailCaptcha")
    public ResponseEntity<Object> sendResetEmailCaptcha(@RequestParam String email) {
        emailService.sendEmail(captchaService.renderCodeTemplate(email, SystemCaptchaBizEnum.EMAIL_RESET_EMAIL_CODE.getRedisKey()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("重置密码，发送验证码")
    @PostMapping(value = "/sendResetPasswordCaptcha")
    public ResponseEntity<Object> sendResetPasswordCaptcha(@RequestParam String email) {
        emailService.sendEmail(captchaService.renderCodeTemplate(email, SystemCaptchaBizEnum.EMAIL_RESET_PWD_CODE.getRedisKey()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("验证码验证")
    @PostMapping(value = "/checkCodeAvailable")
    public ResponseEntity<Object> checkCodeAvailable(@RequestParam String email, @RequestParam String code, @RequestParam String codeBi) {
        SystemCaptchaBizEnum biEnum = SystemCaptchaBizEnum.getByBizCode(codeBi);
        switch (Objects.requireNonNull(biEnum)) {
            case EMAIL_RESET_EMAIL_CODE:
                captchaService.checkCodeAvailable(SystemCaptchaBizEnum.EMAIL_RESET_EMAIL_CODE.getBizCode(), email, code);
                break;
            case EMAIL_RESET_PWD_CODE:
                captchaService.checkCodeAvailable(SystemCaptchaBizEnum.EMAIL_RESET_PWD_CODE.getBizCode(), email, code);
                break;
            default:
                break;
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
