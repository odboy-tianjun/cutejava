package cn.odboy.application.system.mapper;

import cn.odboy.model.system.domain.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Set;


@Mapper
public interface RoleMenuMapper {
    void insertBatchWithRoleId(@Param("menus") Set<Menu> menus, @Param("roleId") Long roleId);
    void deleteByRoleId(@Param("roleId") Long roleId);

    void deleteByRoleIds(@Param("roleIds") Set<Long> roleIds);

    void deleteByMenuId(@Param("menuId") Long menuId);
}
