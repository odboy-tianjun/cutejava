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

import cn.odboy.base.CsBaseUserTimeTb;
import cn.odboy.system.constant.SystemDataScopeEnum;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

/**
 * 角色
 */
@Getter
@Setter
@TableName("system_role")
public class SystemRoleTb extends CsBaseUserTimeTb {

    @NotNull(groups = {Update.class})
    @TableId(value = "role_id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @TableField(exist = false)
    @ApiModelProperty(value = "用户", hidden = true)
    private Set<SystemUserTb> users;

    @TableField(exist = false)
    @ApiModelProperty(value = "菜单", hidden = true)
    private Set<SystemMenuTb> menus;

    @TableField(exist = false)
    @ApiModelProperty(value = "部门", hidden = true)
    private Set<SystemDeptTb> depts;

    @NotBlank
    @ApiModelProperty(value = "名称", hidden = true)
    private String name;

    @ApiModelProperty(value = "数据权限，全部 、 本级 、 自定义")
    private String dataScope = SystemDataScopeEnum.THIS_LEVEL.getValue();

    @ApiModelProperty(value = "级别，数值越小，级别越大")
    private Integer level = 3;

    @ApiModelProperty(value = "描述")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SystemRoleTb role = (SystemRoleTb) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
