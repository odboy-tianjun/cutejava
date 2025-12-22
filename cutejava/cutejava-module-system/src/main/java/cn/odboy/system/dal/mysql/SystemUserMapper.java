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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SystemUserMapper extends BaseMapper<SystemUserTb> {
    List<SystemUserTb> selectUserByArgs(@Param("criteria") SystemQueryUserArgs criteria);
    IPage<SystemUserTb> selectUserByArgs(@Param("criteria") SystemQueryUserArgs criteria, Page<SystemUserTb> page);
    List<SystemUserTb> selectUserByRoleId(@Param("roleId") Long roleId);
    List<SystemUserTb> selectUserByDeptId(@Param("deptId") Long deptId);
    List<SystemUserTb> selectUserByMenuId(@Param("menuId") Long menuId);
    Long countUserByJobIds(@Param("jobIds") Set<Long> jobIds);
    Long countUserByRoleIds(@Param("roleIds") Set<Long> roleIds);
    SystemUserTb getUserByUsername(@Param("username") String username);
    default long countUserByDeptIds(Set<Long> deptIds) {
        LambdaQueryWrapper<SystemUserTb> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SystemUserTb::getDeptId, deptIds);
        return selectCount(wrapper);
    }
    default SystemUserTb getUserByEmail(String email) {
        LambdaQueryWrapper<SystemUserTb> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemUserTb::getEmail, email);
        wrapper.eq(SystemUserTb::getEnabled, 1);
        return selectOne(wrapper);
    }
    default SystemUserTb getUserByPhone(String phone) {
        LambdaQueryWrapper<SystemUserTb> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemUserTb::getPhone, phone);
        wrapper.eq(SystemUserTb::getEnabled, 1);
        return selectOne(wrapper);
    }
    default void updateUserPasswordByUsername(String username, String password) {
        LambdaUpdateWrapper<SystemUserTb> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SystemUserTb::getUsername, username);
        wrapper.set(SystemUserTb::getPassword, password);
        update(null, wrapper);
    }
    default void updateUserEmailByUsername(String username, String email) {
        LambdaUpdateWrapper<SystemUserTb> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SystemUserTb::getUsername, username);
        wrapper.set(SystemUserTb::getEmail, email);
        update(null, wrapper);
    }
    default void updateUserPasswordByUserIds(String password, Set<Long> userIds) {
        LambdaUpdateWrapper<SystemUserTb> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(SystemUserTb::getId, userIds);
        wrapper.set(SystemUserTb::getPassword, password);
        update(null, wrapper);
    }
}
