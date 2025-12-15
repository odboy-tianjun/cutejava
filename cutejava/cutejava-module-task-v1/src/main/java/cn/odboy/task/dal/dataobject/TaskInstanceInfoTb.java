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
import org.springframework.data.annotation.CreatedBy;

import java.util.Date;

/**
 * <p>
 * 任务实例
 * </p>
 *
 * @author codegen
 * @since 2025-09-26
 */
@Getter
@Setter
@ToString
@TableName("task_instance_info")
@ApiModel(value = "TaskInstanceInfoTb对象", description = "任务实例")
public class TaskInstanceInfoTb extends KitObject {
    @CreatedBy
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建人", hidden = true)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间: yyyy-MM-dd HH:mm:ss", hidden = true)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间: yyyy-MM-dd HH:mm:ss", hidden = true)
    private Date updateTime;

    /**
     * id, @JsonSerialize和@JSONField，用于处理大数精度丢失问题
     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @JSONField(format = "string")
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    @TableField("context_name")
    private String contextName;

    /**
     * 语言
     */
    @ApiModelProperty("语言")
    @TableField("`language`")
    private String language;

    /**
     * 变更类型
     */
    @ApiModelProperty("变更类型")
    @TableField("change_type")
    private String changeType;

    /**
     * 环境别名
     */
    @TableField("env_alias")
    @ApiModelProperty("环境别名")
    private String envAlias;

    /**
     * 状态(running进行中 success成功 fail失败)
     */
    @TableField("`status`")
    @ApiModelProperty("状态(running进行中 success成功 fail失败)")
    private String status;

    /**
     * 完成时间
     */
    @ApiModelProperty("完成时间")
    @TableField("finish_time")
    private Date finishTime;

    /**
     * 来源
     */
    @ApiModelProperty("来源")
    @TableField("`source`")
    private String source;

    /**
     * 变更原因
     */
    @TableField("reason")
    @ApiModelProperty("变更原因")
    private String reason;

    /**
     * 任务模板
     */
    @TableField("template")
    @ApiModelProperty("template")
    private String template;

    /**
     * QuartzJob参数
     */
    @TableField("job_data")
    @ApiModelProperty("QuartzJob参数")
    private String jobData;

    /**
     * 异常信息
     */
    @TableField("error_message")
    @ApiModelProperty("异常信息")
    private String errorMessage;
}
