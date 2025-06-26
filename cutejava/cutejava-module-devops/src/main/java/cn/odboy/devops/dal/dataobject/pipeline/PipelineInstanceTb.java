package cn.odboy.devops.dal.dataobject.pipeline;

import cn.odboy.base.CsObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <p>
 * 流水线实例
 * </p>
 *
 * @author codegen
 * @since 2025-06-26
 */
@Getter
@Setter
@TableName("pipeline_instance")
@ApiModel(value = "PipelineInstance对象", description = "流水线实例")
public class PipelineInstanceTb extends CsObject{

    /**
     * 流水线模板id
     */
    @ApiModelProperty("流水线模板id")
    @TableField("pipeline_template_id")
    private Long pipelineTemplateId;

    /**
     * 流水线实例名称
     */
    @ApiModelProperty("流水线实例名称")
    @TableField("pipeline_instance_name")
    private String pipelineInstanceName;

    /**
     * 流水线实例id
     */
    @ApiModelProperty("流水线实例id")
    @TableField("pipeline_instance_id")
    private String pipelineInstanceId;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private Date createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    @TableField("create_by")
    private String createBy;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    @TableField("update_time")
    private Date updateTime;

    /**
     * 流水线模板类型
     */
    @TableField("`type`")
    @ApiModelProperty("流水线模板类型")
    private String type;

    /**
     * 应用名称
     */
    @TableField("app_name")
    @ApiModelProperty("应用名称")
    private String appName;

    /**
     * 环境编码
     */
    @TableField("env")
    @ApiModelProperty("环境编码")
    private String env;

    /**
     * 流水线实例状态
     */
    @TableField("`status`")
    @ApiModelProperty("流水线实例状态")
    private String status;

    /**
     * 流水线实例当前节点code
     */
    @TableField("current_node")
    @ApiModelProperty("流水线实例当前节点code")
    private String currentNode;

    /**
     * 流水线实例当前节点状态
     */
    @ApiModelProperty("流水线实例当前节点状态")
    @TableField("current_node_status")
    private String currentNodeStatus;

    /**
     * 输入参数
     */
    @TableField("`context`")
    @ApiModelProperty("输入参数")
    private String context;

    /**
     * 流水线模板
     */
    @TableField("pipeline_template")
    @ApiModelProperty("流水线模板")
    private String pipelineTemplate;
}
