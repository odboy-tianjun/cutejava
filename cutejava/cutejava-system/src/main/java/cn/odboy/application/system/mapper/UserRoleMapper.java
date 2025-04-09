package cn.odboy.application.system.mapper;

import cn.odboy.model.system.domain.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Set;

@Mapper
public interface UserRoleMapper {
    void insertBatchWithUserId(@Param("roles") Set<Role> roles, @Param("userId") Long userId);

    void deleteByUserId(@Param("userId") Long userId);

    void deleteByUserIds(@Param("userIds") Set<Long> userIds);
}
