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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 在线用户
 */
@Data
public class SystemUserOnlineVo {
    @Schema(name = "Token编号")
    private String uid;

    @Schema(name = "用户名")
    private String userName;

    @Schema(name = "昵称")
    private String nickName;

    @Schema(name = "岗位")
    private String dept;

    @Schema(name = "浏览器")
    private String browser;

    @Schema(name = "IP")
    private String ip;

    @Schema(name = "地址")
    private String address;

    @Schema(name = "token")
    private String key;

    @Schema(name = "登录时间")
    private Date loginTime;
}
