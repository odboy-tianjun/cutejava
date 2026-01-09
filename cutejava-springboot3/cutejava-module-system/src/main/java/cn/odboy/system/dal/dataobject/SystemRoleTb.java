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
import cn.odboy.system.constant.SystemDataScopeEnum;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色
 */
@Getter
@Setter
@TableName("system_role")
public class SystemRoleTb extends KitBaseUserTimeTb {

  @NotNull(groups = {Update.class})
  @TableId(value = "role_id", type = IdType.AUTO)
  @ApiModelProperty(value = "ID", hidden = true)
  private Long id;
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
