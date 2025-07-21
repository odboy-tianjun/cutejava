package cn.odboy.devops.dal.dataobject;

import cn.odboy.base.CsObject;
import cn.odboy.devops.constant.pipeline.PipelineStatusEnum;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeOperateButtonVo;
import com.anwen.mongo.annotation.ID;
import com.anwen.mongo.annotation.collection.CollectionField;
import com.anwen.mongo.annotation.collection.CollectionName;
import com.anwen.mongo.enums.FieldFill;
import com.anwen.mongo.enums.IdTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 流水线实例明细
 *
 * @author odboy
 * @date 2025-07-20
 */
@Getter
@Setter
@CollectionName("pipeline_instance_node")
public class PipelineInstanceNodeTb extends CsObject {
    /**
     * mongoid
     */
    @ID(type = IdTypeEnum.ASSIGN_ID)
    private Long id;
    /**
     * 流水线实例id
     */
    @CollectionField("instance_id")
    private Long instanceId;
    /**
     * 节点名称
     */
    @CollectionField("name")
    private String name;
    /**
     * 节点编码，也就是流水线模板中的code
     */
    @CollectionField("code")
    private String code;
    /**
     * 节点类型：默认为service
     */
    @CollectionField("type")
    private String type = "service";
    /**
     * 节点具体服务类型：gitlab、jenkins等
     */
    @CollectionField("detail_type")
    private String detailType;
    /**
     * 默认附加参数
     */
    @CollectionField("parameters")
    private Map<String, Object> parameters;
    @CollectionField("click")
    private Boolean click = false;
    @CollectionField("retry")
    private Boolean retry = false;
    @CollectionField("buttons")
    private List<PipelineNodeOperateButtonVo> buttons;
    @CollectionField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
    @CollectionField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    @CollectionField("start_time")
    private Date startTime;
    /**
     * 节点状态
     */
    @CollectionField("current_node_msg")
    private String currentNodeMsg = PipelineStatusEnum.PENDING.getDesc();
    @CollectionField("current_node_status")
    private String currentNodeStatus = PipelineStatusEnum.PENDING.getCode();
    /**
     * 节点状态
     */
    @CollectionField("status")
    private String status = PipelineStatusEnum.PENDING.getCode();
}
