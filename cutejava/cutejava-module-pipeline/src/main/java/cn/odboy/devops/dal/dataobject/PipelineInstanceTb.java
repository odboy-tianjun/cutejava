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
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
public class PipelineInstanceTb extends CsObject {
    @ApiModelProperty("流水线实例id")
    @TableId(value = "instance_id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long instanceId;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
    @ApiModelProperty("创建人")
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;
    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty("流水线实例名称")
    @TableField("instance_name")
    private String instanceName;

    @TableField("env")
    @ApiModelProperty("环境编码")
    private String env;

    @TableField("context_name")
    @ApiModelProperty("上下文名称")
    private String contextName;
    @TableField("context_params")
    @ApiModelProperty("上下文参数，这里指QuartzJobDataMap")
    private String contextParams;

    @TableField("current_node")
    @ApiModelProperty("流水线实例当前节点code")
    private String currentNode;
    @ApiModelProperty("流水线实例当前节点状态")
    @TableField("current_node_status")
    private String currentNodeStatus;
    @TableField("`status`")
    @ApiModelProperty("流水线实例状态")
    private String status;

    @ApiModelProperty("流水线模板id")
    @TableField("template_id")
    private Long templateId;
    @TableField("template_type")
    @ApiModelProperty("流水线模板类型")
    private String templateType;
    @TableField("template_content")
    @ApiModelProperty("流水线模板")
    private String templateContent;
}
