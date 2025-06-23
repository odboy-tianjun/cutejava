package cn.odboy.core.dal.mysql.system;

import cn.odboy.core.dal.dataobject.system.SystemMenuTb;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;


/**
 * 角色菜单关联 Mapper
 *
 * @author odboy
 */
@Mapper
public interface SystemRoleMenuMapper {
    void batchInsertRoleMenu(@Param("menus") Set<SystemMenuTb> menus, @Param("roleId") Long roleId);

    void batchDeleteRoleMenu(@Param("roleIds") Set<Long> roleIds);

    void deleteRoleMenuByRoleId(@Param("roleId") Long roleId);

    void deleteRoleMenuByMenuId(@Param("menuId") Long menuId);
}
