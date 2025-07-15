package cn.odboy.system.dal.dataobject;

import cn.odboy.base.CsBaseUserTimeTb;
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
public class SystemOssStorageTb extends CsBaseUserTimeTb {
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
