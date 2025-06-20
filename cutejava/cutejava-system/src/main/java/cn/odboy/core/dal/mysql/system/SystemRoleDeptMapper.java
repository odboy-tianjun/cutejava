package cn.odboy.core.dal.mysql.system;

import cn.odboy.core.dal.dataobject.system.SystemDeptTb;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface SystemRoleDeptMapper {
    void insertBatchWithRoleId(@Param("depts") Set<SystemDeptTb> depts, @Param("roleId") Long roleId);

    void deleteByRoleId(@Param("roleId") Long roleId);

    void deleteByRoleIds(@Param("roleIds") Set<Long> roleIds);
}
