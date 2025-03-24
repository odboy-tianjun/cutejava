package cn.odboy.model.system.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 修改密码
 */
@Data
public class UserPassVo {

    @ApiModelProperty(value = "旧密码")
    private String oldPass;

    @ApiModelProperty(value = "新密码")
    private String newPass;
}
