package cn.odboy.system.dal.model;

import cn.odboy.system.dal.dataobject.SystemDeptTb;
import cn.odboy.system.dal.dataobject.SystemMenuTb;
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import cn.odboy.system.dal.dataobject.SystemUserTb;
import io.swagger.annotations.ApiModelProperty;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemRoleVo extends SystemRoleTb {

    @ApiModelProperty(value = "用户", hidden = true) private Set<SystemUserTb> users;
    @ApiModelProperty(value = "菜单", hidden = true) private Set<SystemMenuTb> menus;
    @ApiModelProperty(value = "部门", hidden = true) private Set<SystemDeptTb> depts;
}
