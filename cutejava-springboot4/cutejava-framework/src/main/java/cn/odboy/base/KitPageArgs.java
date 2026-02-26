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
package cn.odboy.base;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModelProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * 数据分页参数
 *
 * @author odboy
 * @date 2025-01-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class KitPageArgs<T> extends KitObject {

  @NotNull(message = "参数page不能为空")
  @Min(value = 1, message = "参数page最小值为1")
  @ApiModelProperty(value = "页码", example = "1")
  private Integer page;
  @NotNull(message = "参数size不能为空")
  @Min(value = 1, message = "参数size最小值为1")
  @ApiModelProperty(value = "每页数据量", example = "10")
  private Integer size;
  private T args;
  @ApiModelProperty(value = "排序参数")
  private OrderBy orderBy;

  @Setter
  public static class OrderBy extends KitObject {

    /**
     * 排序列名称
     */
    private String column;
    /**
     * 排序方式
     */
    private String orderType;

    /**
     * 字段属性转下划线
     *
     * @return /
     */
    public String getColumn() {
      return StrUtil.toUnderlineCase(this.column);
    }

    /**
     * 是否顺序排序
     *
     * @return /
     */
    public boolean isAsc() {
      return "asc".equals(this.orderType);
    }

    /**
     * 绑定 QueryWrapper，应用排序条件
     *
     * @param wrapper QueryWrapper<T>
     */
    public <T> void bindWrapper(QueryWrapper<T> wrapper) {
      wrapper.orderBy(true, this.isAsc(), this.getColumn());
    }
  }
}
