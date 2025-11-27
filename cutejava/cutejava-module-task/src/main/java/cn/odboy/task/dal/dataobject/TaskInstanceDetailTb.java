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

import cn.odboy.base.KitObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * <p>
 * 任务实例明细
 * </p>
 *
 * @author codegen
 * @since 2025-09-26
 */
@Getter
@Setter
@ToString
@TableName("task_instance_detail")
@ApiModel(value = "TaskInstanceDetailTb对象", description = "任务实例明细")
public class TaskInstanceDetailTb extends KitObject {
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间: yyyy-MM-dd HH:mm:ss", hidden = true)
    private Date createTime;

    /**
     * id
     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @JSONField(format = "string")
    private Long id;

    /**
     * 任务实例id
     */
    @TableField("instance_id")
    @ApiModelProperty("任务实例id")
    private Long instanceId;

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
     * 业务编码
     */
    @TableField("biz_code")
    @ApiModelProperty("业务编码")
    private String bizCode;

    /**
     * 业务名称(步骤)
     */
    @TableField("biz_name")
    @ApiModelProperty("业务名称(步骤)")
    private String bizName;

    /**
     * 执行参数
     */
    @ApiModelProperty("执行参数")
    @TableField("execute_params")
    private String executeParams;

    /**
     * 执行信息
     */
    @ApiModelProperty("执行信息")
    @TableField("execute_info")
    private String executeInfo;

    /**
     * 执行状态(running进行中 success成功 fail失败)
     */
    @TableField("execute_status")
    @ApiModelProperty("执行状态(running进行中 success成功 fail失败)")
    private String executeStatus;
}
