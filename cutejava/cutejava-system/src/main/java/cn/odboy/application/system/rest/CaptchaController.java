package cn.odboy.application.system.rest;

import cn.odboy.application.tools.service.CaptchaService;
import cn.odboy.application.tools.service.EmailService;
import cn.odboy.constant.CodeBiEnum;
import cn.odboy.constant.CodeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/code")
@Api(tags = "系统：验证码管理")
public class CaptchaController {
    private final CaptchaService captchaService;
    private final EmailService emailService;

    /**
     * 除了这个其他的接口都没有用上
     *
     * @param email 邮件地址
     */
    @PostMapping(value = "/sendCaptcha")
    @ApiOperation("重置邮箱，发送验证码")
    public ResponseEntity<Object> resetEmail(@RequestParam String email) {
        emailService.sendCaptcha(email, CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/resetPass")
    @ApiOperation("重置密码，发送验证码")
    public ResponseEntity<Object> resetPass(@RequestParam String email) {
        emailService.sendCaptcha(email, CodeEnum.EMAIL_RESET_PWD_CODE.getKey());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/validated")
    @ApiOperation("验证码验证")
    public ResponseEntity<Object> validated(@RequestParam String email, @RequestParam String code, @RequestParam Integer codeBi) {
        CodeBiEnum biEnum = CodeBiEnum.find(codeBi);
        switch (Objects.requireNonNull(biEnum)) {
            case ONE:
                captchaService.checkCode(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey(), email, code);
                break;
            case TWO:
                captchaService.checkCode(CodeEnum.EMAIL_RESET_PWD_CODE.getKey(), email, code);
                break;
            default:
                break;
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
