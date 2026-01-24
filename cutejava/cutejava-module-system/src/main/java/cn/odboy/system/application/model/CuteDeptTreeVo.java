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
package cn.odboy.system.application.model;

import cn.odboy.base.KitObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CuteDeptTreeVo extends KitObject {

  /**
   * 部门ID
   */
  private String id;
  /**
   * 部门名称
   */
  private String name;
  /**
   * 上级部门ID
   */
  private String pid;
  /**
   * 子节点数目
   */
  private Integer subCount = 0;

  /**
   * 是否有子节点
   */
  public Boolean getHasChildren() {
    return subCount > 0;
  }

  /**
   * 是否为叶子
   */
  public Boolean getLeaf() {
    return subCount <= 0;
  }

  /**
   * 标签名称
   */
  public String getLabel() {
    return name;
  }
}
