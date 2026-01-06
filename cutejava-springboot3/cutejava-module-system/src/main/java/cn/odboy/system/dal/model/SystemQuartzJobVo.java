package cn.odboy.system.dal.model;

import cn.odboy.system.dal.dataobject.SystemQuartzJobTb;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemQuartzJobVo extends SystemQuartzJobTb {

  @ApiModelProperty(value = "用于子任务唯一标识", hidden = true)
  private String uuid;
}
