package cn.odboy.devops.dal.dataobject;

import cn.odboy.base.CsObject;
import com.anwen.mongo.annotation.ID;
import com.anwen.mongo.annotation.collection.CollectionField;
import com.anwen.mongo.annotation.collection.CollectionName;
import com.anwen.mongo.enums.FieldFill;
import com.anwen.mongo.enums.IdTypeEnum;
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
@CollectionName("pipeline_instance_node_detail")
public class PipelineInstanceNodeDetailTb extends CsObject {
    /**
     * mongoid
     */
    @ID(type = IdTypeEnum.ASSIGN_ID)
    private Long id;
    /**
     * 流水线节点Id
     */
    @CollectionField("node_id")
    private Long nodeId;

    /**
     * 开始时间
     */
    @CollectionField(value = "start_time", fill = FieldFill.INSERT)
    private Date startTime;

    /**
     * 结束时间
     */
    @CollectionField("finish_time")
    private Date finishTime;

    /**
     * 节点编码
     */
    @CollectionField("node_code")
    private String nodeCode;
    /**
     * 步骤描述
     */
    @CollectionField("step_name")
    private String stepName;

    /**
     * 步骤状态
     */
    @CollectionField("step_status")
    private String stepStatus;

    /**
     * 异常明细
     */
    @CollectionField("step_msg")
    private String stepMsg;
}
