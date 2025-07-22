package cn.odboy.devops.dal.redis;

import cn.odboy.devops.constant.pipeline.PipelineConst;
import cn.odboy.devops.dal.dataobject.PipelineInstanceTb;
import cn.odboy.framework.redis.RedisHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PipelineInstanceDAO {
    private final RedisHelper redisHelper;

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

    public void unLock(PipelineInstanceTb pipelineInstanceTb) {
        String redisKey = PipelineConst.INSTANCE_PREFIX + pipelineInstanceTb.getTemplateId() + pipelineInstanceTb.getContextName();
        Object o = redisHelper.get(redisKey);
        if (o != null) {
            redisHelper.del(redisKey);
        }
    }
}
