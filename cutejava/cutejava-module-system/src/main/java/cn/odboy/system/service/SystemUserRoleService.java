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
package cn.odboy.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.system.dal.dataobject.SystemDeptTb;
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import cn.odboy.system.dal.dataobject.SystemUserRoleTb;
import cn.odboy.system.dal.model.response.SystemMenuVo;
import cn.odboy.system.dal.model.response.SystemRoleVo;
import cn.odboy.system.dal.model.response.SystemUserVo;
import cn.odboy.system.dal.mysql.SystemRoleMapper;
import cn.odboy.system.dal.mysql.SystemUserRoleMapper;
import cn.odboy.system.framework.permission.core.KitSecurityHelper;
import cn.odboy.util.KitBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SystemUserRoleService {

    @Autowired
    private SystemRoleMapper systemRoleMapper;
    @Autowired
    private SystemUserRoleMapper systemUserRoleMapper;
    @Autowired
    private SystemRoleMenuService systemRoleMenuService;
    @Autowired
    private SystemRoleDeptService systemRoleDeptService;

    @Transactional(rollbackFor = Exception.class)
    public void batchInsertUserRole(Set<SystemRoleTb> roles, Long userId) {
        if (CollUtil.isNotEmpty(roles)) {
            List<SystemUserRoleTb> records = new ArrayList<>();
            for (SystemRoleTb role : roles) {
                SystemUserRoleTb record = new SystemUserRoleTb();
                record.setUserId(userId);
                record.setRoleId(role.getId());
                records.add(record);
            }
            systemUserRoleMapper.insert(records);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteUserRole(Set<Long> userIds) {
        if (CollUtil.isNotEmpty(userIds)) {
            LambdaQueryWrapper<SystemUserRoleTb> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(SystemUserRoleTb::getUserId, userIds);
            systemUserRoleMapper.delete(wrapper);
        }
    }

    /**
     * 转换为SystemRoleVo并关联查询菜单和部门信息
     *
     * @param role 角色基本信息
     * @return 包含关联信息的SystemRoleVo
     */
    public SystemRoleVo convertToRoleVo(SystemRoleTb role) {
        if (role == null) {
            return null;
        }
        SystemRoleVo roleVo = KitBeanUtil.copyToClass(role, SystemRoleVo.class);
        // 查询关联的菜单信息
        Set<SystemMenuVo> menus = systemRoleMenuService.queryMenuByRoleIds(Collections.singleton(role.getId()));
        roleVo.setMenus(menus);
        // 查询关联的部门信息
        Set<SystemDeptTb> depts = systemRoleDeptService.listUserDeptByRoleId(role.getId());
        roleVo.setDepts(depts);
        return roleVo;
    }

    /**
     * 根据用户ID查询
     *
     * @param userId 用户ID
     * @return /
     */
    public List<SystemRoleVo> queryRoleVoByUsersId(Long userId) {
        Set<SystemRoleTb> roles = this.listUserRoleByUserId(userId);
        return roles.stream().map(this::convertToRoleVo).collect(Collectors.toList());
    }

    /**
     * 根据角色查询角色级别
     *
     * @param roles /
     * @return /
     */
    public Integer getDeptLevelByRoles(Set<SystemRoleTb> roles) {
        if (CollUtil.isEmpty(roles)) {
            // 最小权限
            return Integer.MAX_VALUE;
        }
        List<Long> roleIds =
            roles.stream().map(SystemRoleTb::getId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollUtil.isEmpty(roleIds)) {
            // 最小权限
            return Integer.MAX_VALUE;
        }
        List<Integer> roleLevels = systemRoleMapper.selectList(
                new LambdaQueryWrapper<SystemRoleTb>().select(SystemRoleTb::getLevel).isNotNull(SystemRoleTb::getLevel)
                    .in(SystemRoleTb::getId, roleIds)).stream().map(SystemRoleTb::getLevel).distinct()
            .collect(Collectors.toList());
        if (CollUtil.isEmpty(roleLevels)) {
            // 最小权限
            return Integer.MAX_VALUE;
        }
        return Collections.min(roleLevels);
    }

    /**
     * 如果当前用户的角色级别低于创建用户的角色级别，则抛出权限不足的错误
     *
     * @param args /
     */
    public void checkLevel(SystemUserVo args) {
        Integer currentLevel = Collections.min(
            this.queryRoleVoByUsersId(KitSecurityHelper.getCurrentUserId()).stream().map(SystemRoleVo::getLevel)
                .collect(Collectors.toList()));
        Integer optLevel = this.getDeptLevelByRoles(args.getRoles());
        if (currentLevel > optLevel) {
            throw new BadRequestException("角色权限不足");
        }
    }

    public long countUserByRoleIds(Set<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return 0L;
        }
        LambdaQueryWrapper<SystemUserRoleTb> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SystemUserRoleTb::getRoleId, ids);
        return systemUserRoleMapper.selectCount(wrapper);
    }

    public Set<SystemRoleTb> listUserRoleByUserId(Long userId) {
        return systemUserRoleMapper.listUserRoleByUserId(userId);
    }
}
