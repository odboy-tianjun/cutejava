package cn.odboy.core.dal.mysql.system;

import cn.odboy.core.dal.dataobject.system.SystemUserTb;
import cn.odboy.core.dal.model.system.QuerySystemUserArgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Mapper
public interface SystemUserMapper extends BaseMapper<SystemUserTb> {

    Long getUserCountByArgs(@Param("criteria") QuerySystemUserArgs criteria);

    IPage<SystemUserTb> queryUserPageByArgs(@Param("criteria") QuerySystemUserArgs criteria, Page<SystemUserTb> page);

    SystemUserTb getUserByUsername(@Param("username") String username);

    SystemUserTb getUserByEmail(@Param("email") String email);

    SystemUserTb getUserByPhone(@Param("phone") String phone);

    void updatePasswordByUsername(@Param("username") String username, @Param("password") String password, @Param("lastPasswordResetTime") Date lastPasswordResetTime);

    void updateEmailByUsername(@Param("username") String username, @Param("email") String email);

    List<SystemUserTb> queryUserListByRoleId(@Param("roleId") Long roleId);

    List<SystemUserTb> queryUserListByDeptId(@Param("deptId") Long deptId);

    List<SystemUserTb> queryUserListByMenuId(@Param("menuId") Long menuId);

    int getUserCountByJobIds(@Param("jobIds") Set<Long> jobIds);

    int getUserCountByDeptIds(@Param("deptIds") Set<Long> deptIds);

    int getUserCountByRoleIds(@Param("roleIds") Set<Long> roleIds);

    void updatePasswordByUserIds(@Param("pwd") String password, @Param("userIds") Set<Long> userIds);
}
