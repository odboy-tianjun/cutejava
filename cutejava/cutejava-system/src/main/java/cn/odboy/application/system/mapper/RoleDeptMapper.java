package cn.odboy.application.system.mapper;

import cn.odboy.model.system.domain.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Set;

@Mapper
public interface RoleDeptMapper {
    void insertBatchWithRoleId(@Param("depts") Set<Dept> depts, @Param("roleId") Long roleId);
    void deleteByRoleId(@Param("roleId") Long roleId);
    void deleteByRoleIds(@Param("roleIds") Set<Long> roleIds);
}
