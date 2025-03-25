package cn.odboy.modules.system.mapper;

import cn.odboy.model.system.domain.Role;
import cn.odboy.model.system.dto.RoleQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    List<Role> selectByPage();
    Long countByCriteria(@Param("criteria") RoleQueryCriteria criteria);
    List<Role> selectByPage(@Param("criteria") RoleQueryCriteria criteria);
    Role selectById(@Param("roleId") Long roleId);
    Role selectByName(@Param("name") String name);
    List<Role> selectByUserId(@Param("userId") Long userId);
    int countByDeptIds(@Param("deptIds") Set<Long> deptIds);
    @Select("SELECT role.role_id as id FROM sys_role role, sys_roles_menus rm WHERE role.role_id = rm.role_id AND rm.menu_id = #{menuId}")
    List<Role> selectByMenuId(@Param("menuId") Long menuId);

}
