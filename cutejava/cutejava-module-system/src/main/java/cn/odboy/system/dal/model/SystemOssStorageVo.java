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
package cn.odboy.system.dal.model;

import cn.odboy.base.KitBaseUserTimeTb;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemOssStorageVo extends KitBaseUserTimeTb {

  @ApiModelProperty("ID")
  private Long id;
  @ApiModelProperty("类型，比如minio")
  private String serviceType;
  @ApiModelProperty("服务地址")
  private String endpoint;
  @ApiModelProperty("存储桶名称")
  private String bucketName;
  @ApiModelProperty("完整文件名称")
  private String fileName;
  @ApiModelProperty("文件大小, 单位：字节")
  private Long fileSize;
  @ApiModelProperty("文件类型")
  private String fileMime;
  @ApiModelProperty("短文件名")
  private String filePrefix;
  @ApiModelProperty("文件后缀")
  private String fileSuffix;
  @ApiModelProperty("文件md5")
  private String fileMd5;
  @ApiModelProperty("文件链接")
  private String fileUrl;
  @ApiModelProperty("文件编码")
  private String fileCode;
  @ApiModelProperty("对象路径")
  private String objectName;
  /**
   * 扩展字段
   */
  private String fileSizeDesc;
}
