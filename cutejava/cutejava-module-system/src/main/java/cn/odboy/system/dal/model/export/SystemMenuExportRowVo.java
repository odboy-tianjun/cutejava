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
package cn.odboy.system.dal.model.export;

import cn.idev.excel.annotation.ExcelProperty;
import cn.odboy.base.KitObject;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemMenuExportRowVo extends KitObject {

  @ExcelProperty("菜单标题")
  private String title;
  @ExcelProperty("菜单类型")
  private String type;
  @ExcelProperty("权限标识")
  private String permission;
  @ExcelProperty("外链菜单")
  private String iFrame;
  @ExcelProperty("菜单可见")
  private String hidden;
  @ExcelProperty("是否缓存")
  private String cache;
  @ExcelProperty("创建日期")
  private Date createTime;
}
