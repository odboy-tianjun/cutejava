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
public class SystemUserExportRowVo extends KitObject {

  @ExcelProperty("用户名")
  private String username;
  @ExcelProperty("角色")
  private String roles;
  @ExcelProperty("部门")
  private String dept;
  @ExcelProperty("岗位")
  private String jobs;
  @ExcelProperty("邮箱")
  private String email;
  @ExcelProperty("状态")
  private String status;
  @ExcelProperty("手机号码")
  private String mobile;
  @ExcelProperty("修改密码的时间")
  private Date updatePwdTime;
  @ExcelProperty("创建日期")
  private Date createTime;
}
