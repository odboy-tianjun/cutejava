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
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import cn.odboy.system.dal.dataobject.SystemUserRoleTb;
import cn.odboy.system.dal.mysql.SystemUserRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemUserRoleService {
    @Autowired private SystemUserRoleMapper systemUserRoleMapper;

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
}
