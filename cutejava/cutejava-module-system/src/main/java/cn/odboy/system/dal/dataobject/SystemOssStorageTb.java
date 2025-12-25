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
package cn.odboy.system.dal.dataobject;

import cn.odboy.base.KitBaseUserTimeTb;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * OSS存储
 * </p>
 *
 * @author codegen
 * @since 2025-07-15
 */
@Getter
@Setter
@TableName("system_oss_storage")
@ApiModel(value = "SystemOssStorage对象", description = "OSS存储")
public class SystemOssStorageTb extends KitBaseUserTimeTb {

  @ApiModelProperty("ID")
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;
  @TableField("service_type")
  @ApiModelProperty("类型，比如minio")
  private String serviceType;
  @TableField("endpoint")
  @ApiModelProperty("服务地址")
  private String endpoint;
  @TableField("bucket_name")
  @ApiModelProperty("存储桶名称")
  private String bucketName;
  @TableField("file_name")
  @ApiModelProperty("完整文件名称")
  private String fileName;
  @TableField("file_size")
  @ApiModelProperty("文件大小, 单位：字节")
  private Long fileSize;
  @TableField("file_mime")
  @ApiModelProperty("文件类型")
  private String fileMime;
  @ApiModelProperty("短文件名")
  @TableField("file_prefix")
  private String filePrefix;
  @ApiModelProperty("文件后缀")
  @TableField("file_suffix")
  private String fileSuffix;
  @TableField("file_md5")
  @ApiModelProperty("文件md5")
  private String fileMd5;
  @TableField("file_url")
  @ApiModelProperty("文件链接")
  private String fileUrl;
  @TableField("file_code")
  @ApiModelProperty("文件编码")
  private String fileCode;
  @TableField("object_name")
  @ApiModelProperty("对象路径")
  private String objectName;
}
