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
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    @TableField("finish_time")
    private Date finishTime;

    /**
     * 节点状态
     */
    @TableField("`status`")
    @ApiModelProperty("节点状态")
    private String status;

    /**
     * 节点索引
     */
    @ApiModelProperty("节点索引")
    @TableField("node_index")
    private Integer nodeIndex;

    /**
     * 节点编码
     */
    @TableField("node_code")
    @ApiModelProperty("节点编码")
    private String nodeCode;

    /**
     * 节点描述
     */
    @ApiModelProperty("节点描述")
    @TableField("node_description")
    private String nodeDescription;

    /**
     * 输入参数
     */
    @TableField("`context`")
    @ApiModelProperty("输入参数")
    private String context;
}
