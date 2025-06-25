package cn.odboy.core.dal.model;

import cn.odboy.base.CsSerializeObject;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SystemCheckEmailCaptchaArgs extends CsSerializeObject {
    @NotBlank(message = "邮箱不能为空")
    private String email;
    @NotBlank(message = "验证码不能为空")
    private String code;
    @NotBlank(message = "验证码业务标识不能为空")
    private String bizCode;
}
