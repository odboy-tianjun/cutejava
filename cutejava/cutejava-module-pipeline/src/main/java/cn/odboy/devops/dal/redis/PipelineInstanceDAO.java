/*
 *  Copyright 2021-2025 Odboy
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package cn.odboy.devops.dal.redis;

import cn.odboy.devops.framework.pipeline.constant.PipelineConst;
import cn.odboy.devops.dal.dataobject.PipelineInstanceTb;
import cn.odboy.framework.redis.CsRedisHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PipelineInstanceDAO {
    private final CsRedisHelper redisHelper;

    /**
     * 全局锁，对应上下文，只能有一次
     */
    public boolean lock(PipelineInstanceTb pipelineInstanceTb) {
        String redisKey = PipelineConst.INSTANCE_PREFIX + pipelineInstanceTb.getTemplateId() + pipelineInstanceTb.getContextName();
        Object o = redisHelper.get(redisKey);
        if (o != null) {
            return true;
        }
        // 3个月
        redisHelper.set(redisKey, true, 60 * 60 * 24 * 90);
        return false;
    }

    public boolean isLock(PipelineInstanceTb pipelineInstanceTb) {
        String redisKey = PipelineConst.INSTANCE_PREFIX + pipelineInstanceTb.getTemplateId() + pipelineInstanceTb.getContextName();
        Object o = redisHelper.get(redisKey);
        return o != null;
    }

    public void unLock(PipelineInstanceTb pipelineInstanceTb) {
        String redisKey = PipelineConst.INSTANCE_PREFIX + pipelineInstanceTb.getTemplateId() + pipelineInstanceTb.getContextName();
        Object o = redisHelper.get(redisKey);
        if (o != null) {
            redisHelper.del(redisKey);
        }
    }
}
