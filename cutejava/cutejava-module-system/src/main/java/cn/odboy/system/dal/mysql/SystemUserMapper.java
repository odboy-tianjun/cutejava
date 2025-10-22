/*
 * Copyright 2021-2025 Odboy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.odboy.system.dal.mysql;

import cn.odboy.system.dal.dataobject.SystemUserTb;
import cn.odboy.system.dal.model.SystemQueryUserArgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface SystemUserMapper extends BaseMapper<SystemUserTb> {
    List<SystemUserTb> selectUserByArgs(@Param("criteria") SystemQueryUserArgs criteria);

    IPage<SystemUserTb> selectUserByArgs(@Param("criteria") SystemQueryUserArgs criteria, Page<SystemUserTb> page);

    SystemUserTb getUserByUsername(@Param("username") String username);

    SystemUserTb getUserByEmail(@Param("email") String email);

    SystemUserTb getUserByPhone(@Param("phone") String phone);

    List<SystemUserTb> selectUserByRoleId(@Param("roleId") Long roleId);

    List<SystemUserTb> selectUserByDeptId(@Param("deptId") Long deptId);

    List<SystemUserTb> selectUserByMenuId(@Param("menuId") Long menuId);

    Long countUserByJobIds(@Param("jobIds") Set<Long> jobIds);

    Long countUserByDeptIds(@Param("deptIds") Set<Long> deptIds);

    Long countUserByRoleIds(@Param("roleIds") Set<Long> roleIds);

    void updateUserPasswordByUsername(@Param("username") String username, @Param("password") String password);

    void updateUserEmailByUsername(@Param("username") String username, @Param("email") String email);

    void batchUpdatePassword(@Param("password") String password, @Param("userIds") Set<Long> userIds);
}
