/*
 *  Copyright 2021-2025 Odboy
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package cn.odboy.devops.dal.dataobject;

import cn.odboy.base.CsObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.anwen.mongo.annotation.ID;
import com.anwen.mongo.annotation.collection.CollectionField;
import com.anwen.mongo.annotation.collection.CollectionName;
import com.anwen.mongo.enums.FieldFill;
import com.anwen.mongo.enums.IdTypeEnum;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;
    /**
     * 流水线节点Id
     */
    @CollectionField("node_id")
    @JSONField(serializeUsing = ToStringSerializer.class)
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
