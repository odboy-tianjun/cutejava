package cn.odboy.system.dal.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色菜单
 */
@Getter
@Setter
@TableName("system_roles_menus")
public class SystemRoleMenuTb {
    private Long menuId;
    private Long roleId;
}
