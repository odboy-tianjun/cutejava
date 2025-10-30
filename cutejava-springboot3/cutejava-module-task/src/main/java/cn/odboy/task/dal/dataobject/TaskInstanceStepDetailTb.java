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
package cn.odboy.task.dal.dataobject;

import cn.odboy.base.CsObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;


/**
 * <p>
 * 任务实例步骤明细
 * </p>
 *
 * @author codegen
 * @since 2025-09-28
 */
@Getter
@Setter
@ToString
@TableName("task_instance_step_detail")
@Schema(name = "TaskInstanceStepDetailTb对象", description = "")
public class TaskInstanceStepDetailTb extends CsObject {
    @TableField(fill = FieldFill.INSERT)
    @Schema(name = "创建时间: yyyy-MM-dd HH:mm:ss", hidden = true)
    private Date createTime;

    /**
     * id
     */
    @Schema(name="id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @JSONField(format = "string")
    private Long id;

    /**
     * task_instance_detail表id
     */
    @TableField("instance_detail_id")
    @Schema(name="task_instance_detail表id")
    private Long instanceDetailId;

    /**
     * 步骤描述
     */
    @TableField("step_desc")
    @Schema(name="步骤描述")
    private String stepDesc;

    /**
     * 状态(success成功 fail失败)
     */
    @TableField("step_status")
    @Schema(name="状态(success成功 fail失败)")
    private String stepStatus;
}
