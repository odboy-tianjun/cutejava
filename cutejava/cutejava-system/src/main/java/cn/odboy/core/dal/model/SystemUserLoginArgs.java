package cn.odboy.core.dal.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SystemUserLoginArgs {

    @NotBlank
    @ApiModelProperty(value = "用户名")
    private String username;

    @NotBlank
    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "验证码")
    private String code;

    @ApiModelProperty(value = "验证码的key")
    private String uuid = "";
}
