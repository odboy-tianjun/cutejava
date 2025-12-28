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
import cn.odboy.system.dal.dataobject.SystemJobTb;
import cn.odboy.system.dal.dataobject.SystemUserJobTb;
import cn.odboy.system.dal.mysql.SystemUserJobMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemUserJobService {

    @Autowired private SystemUserJobMapper systemUserJobMapper;

    @Transactional(rollbackFor = Exception.class)
    public void batchInsertUserJob(Set<SystemJobTb> jobs, Long userId) {
        if (CollUtil.isNotEmpty(jobs)) {
            List<SystemUserJobTb> records = new ArrayList<>();
            for (SystemJobTb job : jobs) {
                SystemUserJobTb record = new SystemUserJobTb();
                record.setUserId(userId);
                record.setJobId(job.getId());
                records.add(record);
            }
            systemUserJobMapper.insert(records);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteUserJob(Set<Long> userIds) {
        if (CollUtil.isNotEmpty(userIds)) {
            LambdaQueryWrapper<SystemUserJobTb> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(SystemUserJobTb::getUserId, userIds);
            systemUserJobMapper.delete(wrapper);
        }
    }
}
