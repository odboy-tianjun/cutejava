package cn.odboy.application.system.mapper;

import cn.odboy.model.system.domain.Role;
import cn.odboy.model.system.request.QueryRoleRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Set;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    List<Role> queryRoleList();

    List<Role> queryRoleListByArgs(@Param("criteria") QueryRoleRequest criteria);

    Long getRoleCountByArgs(@Param("criteria") QueryRoleRequest criteria);

    Role getRoleByName(@Param("name") String name);

    Role getRoleById(@Param("roleId") Long roleId);

    List<Role> queryRoleListByUserId(@Param("userId") Long userId);

    int getRoleCountByDeptIds(@Param("deptIds") Set<Long> deptIds);

    List<Role> queryRoleListByMenuId(@Param("menuId") Long menuId);
}