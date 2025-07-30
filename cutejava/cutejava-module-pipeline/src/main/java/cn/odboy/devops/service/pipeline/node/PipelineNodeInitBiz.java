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
package cn.odboy.devops.service.pipeline.node;

import cn.hutool.core.thread.ThreadUtil;
import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeTb;
import cn.odboy.devops.framework.pipeline.log.PipelineNodeStepLog;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeJobExecuteResult;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeTemplateVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PipelineNodeInitBiz {
    @PipelineNodeStepLog("初始化开始")
    public void initStart(PipelineInstanceNodeTb pipelineInstanceNode, String contextName, String env, List<PipelineNodeTemplateVo> templateList,
        PipelineNodeJobExecuteResult lastNodeResult) {
        ThreadUtil.safeSleep(1000);
    }

    @PipelineNodeStepLog("Master分支合并到Release分支")
    public void mergeMasterToRelease(PipelineInstanceNodeTb pipelineInstanceNode, String contextName, String env, List<PipelineNodeTemplateVo> templateList,
        PipelineNodeJobExecuteResult lastNodeResult) {
        ThreadUtil.safeSleep(2000);
    }

    @PipelineNodeStepLog("新建Release分支")
    public void createReleaseBranch(PipelineInstanceNodeTb pipelineInstanceNode, String contextName, String env, List<PipelineNodeTemplateVo> templateList,
        PipelineNodeJobExecuteResult lastNodeResult) {
        ThreadUtil.safeSleep(2000);
    }

    @PipelineNodeStepLog("初始化完成")
    public void initFinish(PipelineInstanceNodeTb pipelineInstanceNode, String contextName, String env, List<PipelineNodeTemplateVo> templateList,
        PipelineNodeJobExecuteResult lastNodeResult) {
        ThreadUtil.safeSleep(1000);
    }
}
