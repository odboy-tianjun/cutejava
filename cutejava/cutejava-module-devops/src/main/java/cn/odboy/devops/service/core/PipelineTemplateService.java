package cn.odboy.devops.service.core;

import cn.odboy.devops.dal.dataobject.PipelineTemplateTb;
import cn.odboy.devops.dal.mysql.PipelineTemplateMapper;
import cn.odboy.framework.exception.BadRequestException;
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
        PipelineTemplateTb pipelineTemplateTb = pipelineTemplateMapper.selectById(id);
        if (pipelineTemplateTb == null) {
            throw new BadRequestException("模板不存在");
        }
        return pipelineTemplateTb;
    }

    public String getPipelineTemplateContentById(Long id) {
        PipelineTemplateTb pipelineTemplateTb = getPipelineTemplateById(id);
        return pipelineTemplateTb.getTemplate();
    }
}
