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
package cn.odboy.system.dal.model.request;

import cn.odboy.base.KitObject;
import cn.odboy.system.dal.dataobject.SystemDeptTb;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SystemCreateRoleArgs extends KitObject {

  @NotBlank(message = "角色名称必填")
  private String name;
  @NotNull(message = "角色级别必填")
  private Integer level;
  @NotBlank(message = "数据范围必填")
  private String dataScope;
  private String description;
  /**
   * 关联的部门
   */
  private Long id;
  private Set<SystemDeptTb> depts;
}
