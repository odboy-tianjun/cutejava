package cn.odboy.core.dal.dataobject;

import cn.odboy.base.CsSerializeObject;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 邮件配置类，数据存覆盖式存入数据存
 */
@Data
@TableName("system_email_config")
@EqualsAndHashCode(callSuper = false)
public class SystemEmailConfigTb extends CsSerializeObject {

    @TableId("config_id")
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "邮件服务器SMTP地址")
    private String host;

    @NotBlank
    @ApiModelProperty(value = "邮件服务器 SMTP 端口")
    private String port;

    @NotBlank
    @ApiModelProperty(value = "发件者用户名")
    private String user;

    @NotBlank
    @ApiModelProperty(value = "密码")
    private String password;

    @NotBlank
    @ApiModelProperty(value = "收件人")
    private String fromUser;
}
