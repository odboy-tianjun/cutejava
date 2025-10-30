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

import cn.odboy.base.CsBaseUserTimeTb;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 任务模板
 * </p>
 *
 * @author codegen
 * @since 2025-09-28
 */
@Getter
@Setter
@ToString
@TableName("task_template_info")
@ApiModel(value = "TaskTemplateInfoTb对象", description = "任务模板")
public class TaskTemplateInfoTb extends CsBaseUserTimeTb {

    /**
     * id
     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 数据有效性
     */
    @TableField("available")
    @ApiModelProperty("数据有效性")
    private Boolean available;

    /**
     * 变更类型
     */
    @ApiModelProperty("变更类型")
    @TableField("change_type")
    private String changeType;

    /**
     * 流水线名称
     */
    @TableField("`name`")
    @ApiModelProperty("流水线名称")
    private String name;

    /**
     * 流水线描述
     */
    @ApiModelProperty("流水线描述")
    @TableField("`description`")
    private String description;

    /**
     * 流水线模板内容
     */
    @TableField("template")
    @ApiModelProperty("流水线模板内容")
    private String template;

    /**
     * 环境别名
     */
    @TableField("env_alias")
    @ApiModelProperty("环境别名")
    private String envAlias;

    /**
     * 应用为语言，资源为类型
     */
    @TableField("`language`")
    @ApiModelProperty("应用为语言，资源为类型")
    private String language;
}
