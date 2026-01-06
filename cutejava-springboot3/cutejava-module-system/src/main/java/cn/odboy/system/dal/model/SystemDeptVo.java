package cn.odboy.system.dal.model;

import cn.odboy.system.dal.dataobject.SystemDeptTb;
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemDeptVo extends SystemDeptTb {

  @ApiModelProperty(value = "角色")
  private Set<SystemRoleTb> roles;
  @ApiModelProperty(value = "子部门")
  private List<SystemDeptVo> children;
}
