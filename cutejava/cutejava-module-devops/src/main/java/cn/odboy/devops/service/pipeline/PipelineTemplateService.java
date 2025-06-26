package cn.odboy.devops.service.pipeline;

import cn.odboy.devops.dal.dataobject.pipeline.PipelineTemplateTb;
import cn.odboy.devops.dal.mysql.pipeline.PipelineTemplateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 流水线模板 服务实现类
 * </p>
 *
 * @author codegen
 * @since 2025-06-26
 */
@Service
@RequiredArgsConstructor
public class PipelineTemplateService {
    private final PipelineTemplateMapper pipelineTemplateMapper;

    public PipelineTemplateTb getPipelineTemplateById(Long id) {
        return pipelineTemplateMapper.selectById(id);
    }
}
