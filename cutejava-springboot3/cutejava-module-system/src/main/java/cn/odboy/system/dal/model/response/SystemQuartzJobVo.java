package cn.odboy.system.dal.model.response;

import cn.odboy.base.KitBaseUserTimeTb;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemQuartzJobVo extends KitBaseUserTimeTb {

  private Long id;
  @ApiModelProperty(value = "定时器名称")
  private String jobName;
  @ApiModelProperty(value = "Bean名称")
  private String beanName;
  @ApiModelProperty(value = "方法名称")
  private String methodName;
  @ApiModelProperty(value = "参数")
  private String params;
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
  @ApiModelProperty(value = "备注")
  private String description;
  /**
   * 扩展字段
   */
  @ApiModelProperty(value = "用于子任务唯一标识", hidden = true)
  private String uuid;
}
