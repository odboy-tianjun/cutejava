package cn.odboy.core.dal.mysql.system;

import cn.odboy.core.dal.dataobject.system.SystemRoleTb;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface SystemUserRoleMapper {
    void batchInsertUserRole(@Param("roles") Set<SystemRoleTb> roles, @Param("userId") Long userId);

    void batchDeleteUserRole(@Param("userIds") Set<Long> userIds);
}
