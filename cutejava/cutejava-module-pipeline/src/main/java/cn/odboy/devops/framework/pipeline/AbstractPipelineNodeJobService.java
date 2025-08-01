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
package cn.odboy.devops.framework.pipeline;

import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeTb;
import cn.odboy.devops.framework.pipeline.constant.PipelineStatusEnum;
import cn.odboy.devops.service.core.PipelineInstanceNodeDetailService;
import cn.odboy.devops.service.core.PipelineInstanceNodeService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public abstract class AbstractPipelineNodeJobService {
    @Autowired
    private PipelineInstanceNodeService pipelineInstanceNodeService;
    @Autowired
    private PipelineInstanceNodeDetailService pipelineInstanceNodeDetailService;

    public PipelineInstanceNodeTb getCurrentNodeInfo(long instanceId, String code) {
        return pipelineInstanceNodeService.getPipelineInstanceNodeByArgs(instanceId, code);
    }

    public void addPipelineInstanceNodeDetailLog(PipelineInstanceNodeTb pipelineInstanceNode, String stepName, PipelineStatusEnum stepStatus, String stepMsg,
        Date finishTime) {
        if (pipelineInstanceNode != null) {
            pipelineInstanceNodeDetailService.addLog(pipelineInstanceNode.getId(), pipelineInstanceNode.getCode(), stepName, stepStatus, stepMsg, finishTime);
        }
    }
}
