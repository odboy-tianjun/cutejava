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
package cn.odboy.system.dal.model;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

/**
 * 在线用户
 */
@Data
public class SystemUserOnlineVo {

  @ApiModelProperty(value = "Token编号")
  private String uid;
  @ApiModelProperty(value = "用户名")
  private String userName;
  @ApiModelProperty(value = "昵称")
  private String nickName;
  @ApiModelProperty(value = "岗位")
  private String dept;
  @ApiModelProperty(value = "浏览器")
  private String browser;
  @ApiModelProperty(value = "IP")
  private String ip;
  @ApiModelProperty(value = "地址")
  private String address;
  @ApiModelProperty(value = "token")
  private String key;
  @ApiModelProperty(value = "登录时间")
  private Date loginTime;
}
