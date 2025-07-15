package cn.odboy.system.dal.mysql;

import cn.odboy.system.dal.dataobject.SystemRoleTb;
import cn.odboy.system.dal.model.SystemQueryRoleArgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 角色 Mapper
 *
 * @author odboy
 */
@Mapper
public interface SystemRoleMapper extends BaseMapper<SystemRoleTb> {
    List<SystemRoleTb> selectAllRole();

    List<SystemRoleTb> selectRoleByArgs(@Param("criteria") SystemQueryRoleArgs criteria);

    Long countRoleByArgs(@Param("criteria") SystemQueryRoleArgs criteria);

    SystemRoleTb getRoleByName(@Param("name") String name);

    SystemRoleTb getRoleById(@Param("roleId") Long roleId);

    List<SystemRoleTb> selectRoleByUserId(@Param("userId") Long userId);

    Integer countRoleByDeptIds(@Param("deptIds") Set<Long> deptIds);

    List<SystemRoleTb> selectRoleByMenuId(@Param("menuId") Long menuId);
}
