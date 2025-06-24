package cn.odboy.core.dal.mysql;

import cn.odboy.core.dal.dataobject.SystemDeptTb;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * 角色部门关联 Mapper
 *
 * @author odboy
 */
@Mapper
public interface SystemRoleDeptMapper {
    void batchInsertRoleDept(@Param("depts") Set<SystemDeptTb> depts, @Param("roleId") Long roleId);

    void batchDeleteRoleDept(@Param("roleIds") Set<Long> roleIds);
}
