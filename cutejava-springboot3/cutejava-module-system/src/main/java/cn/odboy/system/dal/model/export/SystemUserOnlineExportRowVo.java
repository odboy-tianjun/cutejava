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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemUserOnlineExportRowVo extends KitObject {

  @ExcelProperty("用户名")
  private String userName;
  @ExcelProperty("部门")
  private String dept;
  @ExcelProperty("登录IP")
  private String ip;
  @ExcelProperty("登录地点")
  private String address;
  @ExcelProperty("浏览器")
  private String browser;
  @ExcelProperty("登录日期")
  private String loginTime;
}
