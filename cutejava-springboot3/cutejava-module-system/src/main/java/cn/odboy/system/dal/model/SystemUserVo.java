package cn.odboy.system.dal.model;

import cn.odboy.system.dal.dataobject.SystemDeptTb;
import cn.odboy.system.dal.dataobject.SystemJobTb;
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import cn.odboy.system.dal.dataobject.SystemUserTb;
import io.swagger.annotations.ApiModelProperty;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemUserVo extends SystemUserTb {
  @ApiModelProperty(value = "用户部门")
  private SystemDeptTb dept;
  @ApiModelProperty(value = "用户角色")
  private Set<SystemRoleTb> roles;
  @ApiModelProperty(value = "用户岗位")
  private Set<SystemJobTb> jobs;
}
