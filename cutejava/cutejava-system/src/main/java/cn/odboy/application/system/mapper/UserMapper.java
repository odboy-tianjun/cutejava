package cn.odboy.application.system.mapper;

import cn.odboy.model.system.domain.User;
import cn.odboy.model.system.request.UserQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    Long getUserCountByCriteria(@Param("criteria") UserQueryCriteria criteria);

    IPage<User> queryUserPage(@Param("criteria") UserQueryCriteria criteria, Page<Object> page);

    User getUserByUsername(@Param("username") String username);

    User getUserByEmail(@Param("email") String email);

    User getUserByPhone(@Param("phone") String phone);

    void updatePasswordByUsername(@Param("username") String username, @Param("password") String password, @Param("lastPasswordResetTime") Date lastPasswordResetTime);

    void updateEmailByUsername(@Param("username") String username, @Param("email") String email);

    List<User> selectUserByRoleId(@Param("roleId") Long roleId);

    List<User> selectUserByRoleDeptId(@Param("deptId") Long deptId);

    List<User> selectUserByMenuId(@Param("menuId") Long menuId);

    int getCountByJobs(@Param("jobIds") Set<Long> jobIds);

    int getCountByDepts(@Param("deptIds") Set<Long> deptIds);

    int getCountByRoles(@Param("roleIds") Set<Long> roleIds);

    void resetPasswordByUserIds(@Param("userIds") Set<Long> userIds, @Param("pwd") String password);
}
