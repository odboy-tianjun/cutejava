package cn.odboy.core.dal.mysql.system;

import cn.odboy.core.dal.dataobject.system.SystemRoleTb;
import cn.odboy.core.dal.model.system.QuerySystemRoleArgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface SystemRoleMapper extends BaseMapper<SystemRoleTb> {
    List<SystemRoleTb> queryRoleList();

    List<SystemRoleTb> queryRoleListByArgs(@Param("criteria") QuerySystemRoleArgs criteria);

    Long getRoleCountByArgs(@Param("criteria") QuerySystemRoleArgs criteria);

    SystemRoleTb getRoleByName(@Param("name") String name);

    SystemRoleTb getRoleById(@Param("roleId") Long roleId);

    List<SystemRoleTb> queryRoleListByUserId(@Param("userId") Long userId);

    int getRoleCountByDeptIds(@Param("deptIds") Set<Long> deptIds);

    List<SystemRoleTb> queryRoleListByMenuId(@Param("menuId") Long menuId);
}
