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
import java.util.List;

@Getter
@Setter
public class CuteProductLineSelectProVo extends KitObject {
  /**
   * 部门ID（select的value）
   */
  private String value;
  /**
   * 产品线，如"研发部/华南分部/devops组"
   */
  private String label;
  private List<CuteProductLineSelectProVo> children;
}
