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
import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@TableName("system_menu")
public class SystemMenuTb extends CsBaseUserTimeTb {

    @NotNull(groups = {Update.class})
    @TableId(value = "menu_id", type = IdType.AUTO)
    @Schema(name ="ID", hidden = true)
    private Long id;

    @TableField(exist = false)
    @JSONField(serialize = false)
    @Schema(name ="菜单角色")
    private Set<SystemRoleTb> roles;

    @TableField(exist = false)
    private List<SystemMenuTb> children;

    @Schema(name ="菜单标题")
    private String title;

    @TableField(value = "name")
    @Schema(name ="菜单组件名称")
    private String componentName;

    @Schema(name ="排序")
    private Integer menuSort = 999;

    @Schema(name ="组件路径")
    private String component;

    @Schema(name ="路由地址")
    private String path;

    @Schema(name ="菜单类型，目录、菜单、按钮")
    private Integer type;

    @Schema(name ="权限标识")
    private String permission;

    @Schema(name ="菜单图标")
    private String icon;

    @Schema(name ="缓存")
    private Boolean cache;

    @Schema(name ="是否隐藏")
    private Boolean hidden;

    @Schema(name ="上级菜单")
    private Long pid;

    @Schema(name ="子节点数目", hidden = true)
    private Integer subCount = 0;

    @Schema(name ="外链菜单")
    private Boolean iFrame;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SystemMenuTb menu = (SystemMenuTb)o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Schema(name ="是否有子节点")
    public Boolean getHasChildren() {
        return subCount > 0;
    }

    @Schema(name ="是否为叶子")
    public Boolean getLeaf() {
        return subCount <= 0;
    }

    @Schema(name ="标签名称")
    public String getLabel() {
        return title;
    }
}
