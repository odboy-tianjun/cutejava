/*
 * Copyright 2021-2025 Odboy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.odboy.system.dal.dataobject;

import cn.odboy.base.KitObject;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 邮件配置类，数据存覆盖式存入数据存
 */
@Data
@TableName("system_email_config")
@EqualsAndHashCode(callSuper = false)
public class SystemEmailConfigTb extends KitObject {

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
