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

package cn.odboy.system.dal.dataobject;

import cn.odboy.base.KitBaseUserTimeTb;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("system_quartz_job")
public class SystemQuartzJobTb extends KitBaseUserTimeTb {

    public static final String JOB_KEY = "JOB_KEY";

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = {Update.class})
    private Long id;

    @TableField(exist = false)
    @Schema(name = "用于子任务唯一标识", hidden = true)
    private String uuid;

    @Schema(name = "定时器名称")
    private String jobName;

    @NotBlank
    @Schema(name = "Bean名称")
    private String beanName;

    @NotBlank
    @Schema(name = "方法名称")
    private String methodName;

    @Schema(name = "参数")
    private String params;

    @NotBlank
    @Schema(name = "cron表达式")
    private String cronExpression;

    @Schema(name = "状态：暂停或启动")
    private Boolean isPause = false;

    @Schema(name = "负责人")
    private String personInCharge;

    @Schema(name = "报警邮箱")
    private String email;

    @Schema(name = "子任务")
    private String subTask;

    @Schema(name = "失败后暂停")
    private Boolean pauseAfterFailure;

    @NotBlank
    @Schema(name = "备注")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String description;
}