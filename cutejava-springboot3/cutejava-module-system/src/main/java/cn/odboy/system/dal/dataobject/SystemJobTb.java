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
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("system_job")
public class SystemJobTb extends KitBaseUserTimeTb {

  @NotNull(groups = Update.class)
  @TableId(value = "id", type = IdType.AUTO)
  @ApiModelProperty(value = "ID", hidden = true)
  private Long id;
  @NotBlank
  @ApiModelProperty(value = "岗位名称")
  private String name;
  @NotNull
  @ApiModelProperty(value = "岗位排序")
  private Long jobSort;
  @NotNull
  @ApiModelProperty(value = "是否启用")
  private Boolean enabled;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SystemJobTb job = (SystemJobTb) o;
    return Objects.equals(id, job.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
