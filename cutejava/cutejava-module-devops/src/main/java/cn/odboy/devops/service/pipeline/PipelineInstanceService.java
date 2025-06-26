package cn.odboy.devops.service.pipeline;

import cn.odboy.devops.dal.dataobject.pipeline.PipelineInstanceTb;
import cn.odboy.devops.dal.mysql.pipeline.PipelineInstanceMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 流水线实例 服务实现类
 * </p>
 *
 * @author codegen
 * @since 2025-06-26
 */
@Service
@RequiredArgsConstructor
public class PipelineInstanceService {
    private final PipelineInstanceMapper pipelineInstanceMapper;
    public PipelineInstanceTb getPipelineInstance(String pipelineInstanceId) {
        return pipelineInstanceMapper.selectOne(new LambdaQueryWrapper<PipelineInstanceTb>()
                .eq(PipelineInstanceTb::getPipelineInstanceId, pipelineInstanceId)
        );
    }

    public void createPipelineInstance(PipelineInstanceTb pipelineInstanceTb) {
        pipelineInstanceMapper.insert(pipelineInstanceTb);
    }
}
