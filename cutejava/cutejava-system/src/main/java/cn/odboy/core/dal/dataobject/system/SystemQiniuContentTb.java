package cn.odboy.core.dal.dataobject.system;

import cn.odboy.base.CsSerializeObject;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 上传成功后，存储结果
 */
@Data
@TableName("system_qiniu_content")
@EqualsAndHashCode(callSuper = false)
public class SystemQiniuContentTb extends CsSerializeObject {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @TableField("name")
    @ApiModelProperty(value = "文件名")
    private String key;

    @ApiModelProperty(value = "空间名")
    private String bucket;

    @ApiModelProperty(value = "大小")
    private String size;

    @ApiModelProperty(value = "文件地址")
    private String url;

    @ApiModelProperty(value = "文件类型")
    private String suffix;

    @ApiModelProperty(value = "空间类型：公开/私有")
    private String type = "公开";

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "创建或更新时间")
    private Date updateTime;
}
