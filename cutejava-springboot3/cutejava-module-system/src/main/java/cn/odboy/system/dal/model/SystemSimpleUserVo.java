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

import cn.odboy.base.KitObject;
import cn.odboy.system.dal.dataobject.SystemDeptTb;
import cn.odboy.system.dal.dataobject.SystemJobTb;
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
public class SystemSimpleUserVo extends KitObject {
    @Schema(name = "ID", hidden = true)
    private Long id;
    @Schema(name = "用户角色")
    private Set<SystemRoleTb> roles;
    @Schema(name = "用户岗位")
    private Set<SystemJobTb> jobs;
    private Long deptId;
    @TableField(exist = false)
    private SystemDeptTb dept;
    @Schema(name = "用户名称")
    private String username;
    @Schema(name = "用户昵称")
    private String nickName;
    @Schema(name = "邮箱")
    private String email;
    @Schema(name = "电话号码")
    private String phone;
    @Schema(name = "用户性别")
    private String gender;
    @Schema(name = "头像真实名称", hidden = true)
    private String avatarName;
    @Schema(name = "是否启用")
    private Boolean enabled;
    @Schema(name = "是否为admin账号", hidden = true)
    private Boolean isAdmin = false;
}