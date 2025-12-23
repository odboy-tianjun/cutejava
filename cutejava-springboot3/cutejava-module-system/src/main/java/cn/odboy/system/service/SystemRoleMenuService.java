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
import cn.odboy.system.dal.dataobject.SystemMenuTb;
import cn.odboy.system.dal.dataobject.SystemRoleMenuTb;
import cn.odboy.system.dal.mysql.SystemMenuMapper;
import cn.odboy.system.dal.mysql.SystemRoleMenuMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemRoleMenuService {
    @Autowired private SystemRoleMenuMapper systemRoleMenuMapper;
    @Autowired private SystemMenuMapper systemMenuMapper;

    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleMenuByRoleId(Long roleId) {
        LambdaQueryWrapper<SystemRoleMenuTb> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemRoleMenuTb::getRoleId, roleId);
        systemRoleMenuMapper.delete(wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchInsertRoleMenu(Set<SystemMenuTb> menus, Long roleId) {
        if (CollUtil.isNotEmpty(menus)) {
            List<SystemRoleMenuTb> records = new ArrayList<>();
            for (SystemMenuTb menu : menus) {
                SystemRoleMenuTb record = new SystemRoleMenuTb();
                record.setMenuId(menu.getId());
                record.setRoleId(roleId);
                records.add(record);
            }
            systemRoleMenuMapper.insert(records);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteRoleMenu(Set<Long> roleIds) {
        if (CollUtil.isNotEmpty(roleIds)) {
            LambdaQueryWrapper<SystemRoleMenuTb> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(SystemRoleMenuTb::getRoleId, roleIds);
            systemRoleMenuMapper.delete(wrapper);
        }
    }

    public LinkedHashSet<SystemMenuTb> queryMenuByRoleIdsAndType(Set<Long> roleIds, int menuType) {
        if (CollUtil.isEmpty(roleIds)) {
            return new LinkedHashSet<>();
        }
        LambdaQueryWrapper<SystemRoleMenuTb> roleMenuWrapper = new LambdaQueryWrapper<>();
        roleMenuWrapper.in(SystemRoleMenuTb::getRoleId, roleIds);
        List<Long> menuIds = systemRoleMenuMapper.selectList(roleMenuWrapper).stream().map(SystemRoleMenuTb::getMenuId)
            .collect(Collectors.toList());
        if (CollUtil.isEmpty(menuIds)) {
            return new LinkedHashSet<>();
        }

        LambdaQueryWrapper<SystemMenuTb> menuWrapper = new LambdaQueryWrapper<>();
        menuWrapper.in(SystemMenuTb::getId, menuIds);
        // 排除"按钮"
        menuWrapper.ne(SystemMenuTb::getType, 2);
        menuWrapper.orderByAsc(SystemMenuTb::getMenuSort);
        return new LinkedHashSet<>(systemMenuMapper.selectList(menuWrapper));
    }
}
