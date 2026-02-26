/*
 * Copyright 2021-2026 Odboy
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
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@TableName("system_dict")
public class SystemDictTb extends KitBaseUserTimeTb {

  @NotNull(groups = Update.class)
  @ApiModelProperty(value = "ID", hidden = true)
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;
  @NotBlank
  @ApiModelProperty(value = "名称")
  private String name;
  @ApiModelProperty(value = "描述")
  @TableField(updateStrategy = FieldStrategy.ALWAYS)
  private String description;
}
