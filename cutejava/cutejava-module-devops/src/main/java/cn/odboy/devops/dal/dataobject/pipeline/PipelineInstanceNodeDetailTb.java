package cn.odboy.devops.dal.dataobject.pipeline;

import cn.odboy.base.CsObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <p>
 * 流水线实例节点明细
 * </p>
 *
 * @author codegen
 * @since 2025-06-26
 */
@Getter
@Setter
@TableName("pipeline_instance_node_detail")
@ApiModel(value = "PipelineInstanceNodeDetail对象", description = "流水线实例节点明细")
public class PipelineInstanceNodeDetailTb extends CsObject {
    /**
     * id
     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)

    private Long id;
    /**
     * 流水线实例id
     */
    @ApiModelProperty("流水线实例id")
    @TableField("pipeline_instance_id")
    private Long pipelineInstanceId;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    @TableField("start_time")
    private Date startTime;

    /**
     * 节点索引
     */
    @ApiModelProperty("节点索引")
    @TableField("node_index")
    private Integer nodeIndex;
    /**
     * 节点编码
     */
    @ApiModelProperty("节点编码")
    @TableField("node_code")
    private String nodeCode;
    /**
     * 步骤描述
     */
    @TableField("step_name")
    @ApiModelProperty("步骤描述")
    private String stepName;

    /**
     * 步骤状态
     */
    @ApiModelProperty("步骤状态")
    @TableField("step_status")
    private String stepStatus;

    /**
     * 异常明细
     */
    @ApiModelProperty("异常明细")
    @TableField("step_msg")
    private String stepMsg;
}
