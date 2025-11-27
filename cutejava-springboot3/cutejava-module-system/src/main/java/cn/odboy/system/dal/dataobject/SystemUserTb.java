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

import cn.odboy.base.KitBaseUserTimeTb;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@TableName("system_user")
public class SystemUserTb extends KitBaseUserTimeTb {

    @NotNull(groups = Update.class)
    @TableId(value = "user_id", type = IdType.AUTO)
    @Schema(name = "ID", hidden = true)
    private Long id;

    @TableField(exist = false)
    @Schema(name = "用户角色")
    private Set<SystemRoleTb> roles;

    @TableField(exist = false)
    @Schema(name = "用户岗位")
    private Set<SystemJobTb> jobs;

    @TableField(value = "dept_id")
    @Schema(hidden = true)
    private Long deptId;

    @Schema(name = "用户部门")
    @TableField(exist = false)
    private SystemDeptTb dept;

    @NotBlank
    @Schema(name = "用户名称")
    private String username;

    @NotBlank
    @Schema(name = "用户昵称")
    private String nickName;

    @Email
    @NotBlank
    @Schema(name = "邮箱")
    private String email;

    @NotBlank
    @Schema(name = "电话号码")
    private String phone;

    @Schema(name = "用户性别")
    private String gender;

    @Schema(name = "头像真实名称", hidden = true)
    private String avatarName;

    @Schema(name = "头像存储的路径", hidden = true)
    private String avatarPath;

    @Schema(name = "密码")
    private String password;

    @NotNull
    @Schema(name = "是否启用")
    private Boolean enabled;

    @Schema(name = "是否为admin账号", hidden = true)
    private Boolean isAdmin = false;

    @Schema(name = "最后修改密码的时间", hidden = true)
    private Date pwdResetTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SystemUserTb user = (SystemUserTb)o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}