package cn.odboy.system.dal.model;

import cn.odboy.system.dal.dataobject.SystemMenuTb;
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class SystemMenuVo extends SystemMenuTb {

  @ApiModelProperty(value = "菜单角色")
  private Set<SystemRoleTb> roles;
  @ApiModelProperty(value = "子菜单")
  private List<SystemMenuVo> children;
}
