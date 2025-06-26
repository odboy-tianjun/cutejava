package cn.odboy.core.dal.mysql;

import cn.odboy.core.dal.dataobject.SystemUserTb;
import cn.odboy.core.dal.model.QuerySystemUserArgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface SystemUserMapper extends BaseMapper<SystemUserTb> {
    IPage<SystemUserTb> selectUserByArgs(@Param("criteria") QuerySystemUserArgs criteria, Page<SystemUserTb> page);

    SystemUserTb getUserByUsername(@Param("username") String username);

    SystemUserTb getUserByEmail(@Param("email") String email);

    SystemUserTb getUserByPhone(@Param("phone") String phone);

    List<SystemUserTb> selectUserByRoleId(@Param("roleId") Long roleId);

    List<SystemUserTb> selectUserByDeptId(@Param("deptId") Long deptId);

    List<SystemUserTb> selectUserByMenuId(@Param("menuId") Long menuId);

    Long countUserByJobIds(@Param("jobIds") Set<Long> jobIds);

    Long countUserByDeptIds(@Param("deptIds") Set<Long> deptIds);

    Long countUserByRoleIds(@Param("roleIds") Set<Long> roleIds);

    Long countUserByArgs(@Param("criteria") QuerySystemUserArgs criteria);

    void updateUserPasswordByUsername(@Param("username") String username, @Param("password") String password);

    void updateUserEmailByUsername(@Param("username") String username, @Param("email") String email);

    void batchUpdatePassword(@Param("password") String password, @Param("userIds") Set<Long> userIds);
}
