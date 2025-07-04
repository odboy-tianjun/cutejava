package cn.odboy.system.dal.dataobject;

import cn.odboy.base.CsObject;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 七牛云对象存储配置类
 */
@Data
@TableName("system_qiniu_config")
@EqualsAndHashCode(callSuper = false)
public class SystemQiniuConfigTb extends CsObject {

    @TableId("config_id")
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "accessKey")
    private String accessKey;

    @NotBlank
    @ApiModelProperty(value = "secretKey")
    private String secretKey;

    @NotBlank
    @ApiModelProperty(value = "存储空间名称作为唯一的 Bucket 识别符")
    private String bucket;

    /**
     * Zone表示与机房的对应关系
     * 华东	Zone.zone0()
     * 华北	Zone.zone1()
     * 华南	Zone.zone2()
     * 北美	Zone.zoneNa0()
     * 东南亚	Zone.zoneAs0()
     */
    @NotBlank
    @ApiModelProperty(value = "Zone表示与机房的对应关系")
    private String zone;

    @NotBlank
    @ApiModelProperty(value = "外链域名，可自定义，需在七牛云绑定")
    private String host;

    @ApiModelProperty(value = "空间类型：公开/私有")
    private String type = "公开";
}
