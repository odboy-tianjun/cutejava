package cn.odboy.modules.system.mapper;

import cn.odboy.model.system.domain.Role;
import cn.odboy.model.system.dto.RoleQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    List<Role> findAllRoles();

    Long countRoles(@Param("criteria") RoleQueryCriteria criteria);

    List<Role> findRoles(@Param("criteria") RoleQueryCriteria criteria);

    Role findRoleById(@Param("roleId") Long roleId);

    Role findRoleByName(@Param("name") String name);

    List<Role> findRolesByUserId(@Param("userId") Long userId);

    int countRolesByDeptIds(@Param("deptIds") Set<Long> deptIds);

    List<Role> findRolesByMenuId(@Param("menuId") Long menuId);
}