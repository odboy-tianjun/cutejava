package cn.odboy.system.dal.mysql;

import cn.odboy.system.dal.dataobject.SystemRoleTb;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface SystemUserRoleMapper {
    void batchInsertUserRole(@Param("roles") Set<SystemRoleTb> roles, @Param("userId") Long userId);

    void batchDeleteUserRole(@Param("userIds") Set<Long> userIds);
}
