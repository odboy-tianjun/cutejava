package cn.odboy.system.dal.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
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
    @TableField(value = "role_id") private Long roleId;
    @TableField(value = "menu_id") private Long menuId;
}
