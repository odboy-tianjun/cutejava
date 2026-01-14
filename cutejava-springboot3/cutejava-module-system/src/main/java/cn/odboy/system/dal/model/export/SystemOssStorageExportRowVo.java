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
public class SystemOssStorageExportRowVo extends KitObject {

  @ExcelProperty("服务类型")
  private String serviceType;
  @ExcelProperty("服务地址")
  private String endpoint;
  @ExcelProperty("存储桶名称")
  private String bucketName;
  @ExcelProperty("文件名称")
  private String fileName;
  @ExcelProperty("文件大小")
  private String fileSizeDesc;
  @ExcelProperty("文件类型")
  private String fileMime;
  @ExcelProperty("创建人")
  private String createBy;
  @ExcelProperty("创建日期")
  private Date createTime;
}
