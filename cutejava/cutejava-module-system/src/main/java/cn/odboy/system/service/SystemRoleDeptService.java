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
import cn.odboy.system.dal.dataobject.SystemDeptTb;
import cn.odboy.system.dal.dataobject.SystemRoleDeptTb;
import cn.odboy.system.dal.mysql.SystemRoleDeptMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SystemRoleDeptService {

    @Autowired
    private SystemRoleDeptMapper systemRoleDeptMapper;

    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteRoleDept(Set<Long> roleIds) {
        if (CollUtil.isNotEmpty(roleIds)) {
            LambdaQueryWrapper<SystemRoleDeptTb> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(SystemRoleDeptTb::getRoleId, roleIds);
            systemRoleDeptMapper.delete(wrapper);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchInsertRoleDept(Set<SystemDeptTb> depts, Long id) {
        if (CollUtil.isNotEmpty(depts)) {
            List<SystemRoleDeptTb> records = new ArrayList<>();
            for (SystemDeptTb dept : depts) {
                SystemRoleDeptTb record = new SystemRoleDeptTb();
                record.setRoleId(id);
                record.setDeptId(dept.getId());
                records.add(record);
            }
            systemRoleDeptMapper.insert(records);
        }
    }

    /**
     * 根据部门ID统计角色数量
     *
     * @param deptIds 部门ID集合
     * @return /
     */
    public Long countRoleByDeptIds(Set<Long> deptIds) {
        if (CollUtil.isEmpty(deptIds)) {
            return 0L;
        }

        LambdaQueryWrapper<SystemRoleDeptTb> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SystemRoleDeptTb::getDeptId, deptIds);
        return systemRoleDeptMapper.selectCount(wrapper);
    }

    /**
     * 根据角色id查询部门信息
     *
     * @param roleId 角色id
     * @return /
     */
    public Set<SystemDeptTb> listUserDeptByRoleId(Long roleId) {
        return systemRoleDeptMapper.listUserDeptByRoleId(roleId);
    }
}
