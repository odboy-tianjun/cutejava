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
}
