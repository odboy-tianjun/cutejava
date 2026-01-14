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
public class SystemQuartzJobExportRowVo extends KitObject {

  @ExcelProperty("任务名称")
  private String jobName;
  @ExcelProperty("Bean名称")
  private String beanName;
  @ExcelProperty("执行方法")
  private String methodName;
  @ExcelProperty("参数")
  private String params;
  @ExcelProperty("表达式")
  private String cronExpression;
  @ExcelProperty("状态")
  private String isPause;
  @ExcelProperty("描述")
  private String description;
  @ExcelProperty("创建日期")
  private Date createTime;
}
