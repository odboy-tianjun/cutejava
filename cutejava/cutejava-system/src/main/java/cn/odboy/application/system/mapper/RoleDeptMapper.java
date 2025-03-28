package cn.odboy.application.system.mapper;

import cn.odboy.model.system.domain.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Set;

@Mapper
public interface RoleDeptMapper {
    void insertBatchByRoleId(@Param("roleId") Long roleId, @Param("depts") Set<Dept> depts);

    void deleteByRoleId(@Param("roleId") Long roleId);

    void deleteBatchByRoleIds(@Param("roleIds") Set<Long> roleIds);
}
