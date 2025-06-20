package cn.odboy.core.controller.system;

import cn.odboy.core.dal.dataobject.system.SystemEmailConfigTb;
import cn.odboy.core.service.system.SystemEmailApi;
import cn.odboy.core.service.system.SystemEmailService;
import cn.odboy.core.dal.model.system.SendSystemEmailArgs;
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
    private final SystemEmailApi emailApi;
    private final SystemEmailService emailService;

    @ApiOperation("查询配置")
    @PostMapping(value = "/describeEmailConfig")
    public ResponseEntity<SystemEmailConfigTb> describeEmailConfig() {
        return new ResponseEntity<>(emailApi.describeEmailConfig(), HttpStatus.OK);
    }

    @ApiOperation("配置邮件")
    @PostMapping(value = "/modifyEmailConfig")
    public ResponseEntity<Object> modifyEmailConfig(@Validated @RequestBody SystemEmailConfigTb emailConfig) throws Exception {
        emailService.modifyEmailConfigOnPassChange(emailConfig);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("发送邮件")
    @PostMapping(value = "/sendEmail")
    public ResponseEntity<Object> sendEmail(@Validated @RequestBody SendSystemEmailArgs sendEmailRequest) {
        emailService.sendEmail(sendEmailRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
