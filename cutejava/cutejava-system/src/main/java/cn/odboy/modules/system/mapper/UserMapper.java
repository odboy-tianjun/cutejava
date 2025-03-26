package cn.odboy.modules.system.mapper;

import cn.odboy.model.system.domain.User;
import cn.odboy.model.system.dto.UserQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    Long countAll(@Param("criteria") UserQueryCriteria criteria);

    IPage<User> findAll(@Param("criteria") UserQueryCriteria criteria, Page<Object> page);

    User findByUsername(@Param("username") String username);

    User findByEmail(@Param("email") String email);

    User findByPhone(@Param("phone") String phone);

    @Select("update sys_user set password = #{password} , pwd_reset_time = #{lastPasswordResetTime} where username = #{username}")
    void updatePass(@Param("username") String username, @Param("password") String password, @Param("lastPasswordResetTime") Date lastPasswordResetTime);

    @Select("update sys_user set email = #{email} where username = #{username}")
    void updateEmail(@Param("username") String username, @Param("email") String email);

    List<User> findByRoleId(@Param("roleId") Long roleId);

    List<User> findByRoleDeptId(@Param("deptId") Long deptId);

    List<User> findByMenuId(@Param("menuId") Long menuId);

    int countByJobs(@Param("jobIds") Set<Long> jobIds);

    int countByDepts(@Param("deptIds") Set<Long> deptIds);

    int countByRoles(@Param("roleIds") Set<Long> roleIds);

    void resetPwd(@Param("userIds") Set<Long> userIds, @Param("pwd") String pwd);
}
