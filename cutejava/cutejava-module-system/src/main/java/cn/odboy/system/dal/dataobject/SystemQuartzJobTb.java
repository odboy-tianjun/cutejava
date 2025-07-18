package cn.odboy.system.dal.dataobject;

import cn.odboy.base.CsBaseUserTimeTb;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@TableName("system_quartz_job")
public class SystemQuartzJobTb extends CsBaseUserTimeTb {

    public static final String JOB_KEY = "JOB_KEY";

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = {Update.class})
    private Long id;

    @TableField(exist = false)
    @ApiModelProperty(value = "用于子任务唯一标识", hidden = true)
    private String uuid;

    @ApiModelProperty(value = "定时器名称")
    private String jobName;

    @NotBlank
    @ApiModelProperty(value = "Bean名称")
    private String beanName;

    @NotBlank
    @ApiModelProperty(value = "方法名称")
    private String methodName;

    @ApiModelProperty(value = "参数")
    private String params;

    @NotBlank
    @ApiModelProperty(value = "cron表达式")
    private String cronExpression;

    @ApiModelProperty(value = "状态：暂停或启动")
    private Boolean isPause = false;

    @ApiModelProperty(value = "负责人")
    private String personInCharge;

    @ApiModelProperty(value = "报警邮箱")
    private String email;

    @ApiModelProperty(value = "子任务")
    private String subTask;

    @ApiModelProperty(value = "失败后暂停")
    private Boolean pauseAfterFailure;

    @NotBlank
    @ApiModelProperty(value = "备注")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String description;
}