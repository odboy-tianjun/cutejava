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
package cn.odboy.system.dal.model.response;

import cn.odboy.base.KitBaseUserTimeTb;
import cn.odboy.system.dal.dataobject.SystemDictTb;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemDictDetailVo extends KitBaseUserTimeTb {

  @ApiModelProperty(value = "ID", hidden = true)
  private Long id;
  @ApiModelProperty(hidden = true)
  private Long dictId;
  @ApiModelProperty(value = "字典标签")
  private String label;
  @ApiModelProperty(value = "字典值")
  private String value;
  @ApiModelProperty(value = "排序")
  private Integer dictSort = 999;
  /**
   * 扩展字段
   */
  @ApiModelProperty(value = "字典")
  private SystemDictTb dict;
}
