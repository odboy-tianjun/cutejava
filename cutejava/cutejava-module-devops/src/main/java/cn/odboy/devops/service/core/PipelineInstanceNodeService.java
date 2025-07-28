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

import cn.odboy.devops.constant.pipeline.PipelineStatusEnum;
import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeTb;
import cn.odboy.devops.dal.dataobject.PipelineInstanceTb;
import com.anwen.mongo.service.IService;

import java.util.Date;
import java.util.List;

/**
 * 流水线实例节点明细
 *
 * @author odboy
 * @date 2025-07-20
 */
public interface PipelineInstanceNodeService extends IService<PipelineInstanceNodeTb> {
    void createPipelineInstanceDetail(PipelineInstanceTb pipelineInstanceTb);

    void updatePipelineInstanceNodeByArgs(Long instanceId, String code, PipelineStatusEnum pipelineStatusEnum, String msg);

    PipelineInstanceNodeTb getPipelineInstanceNodeByArgs(Long instanceId, String code);

    void remakePipelineInstanceDetail(PipelineInstanceTb pipelineInstanceTb, String retryNodeCode);

    List<PipelineInstanceNodeTb> queryPipelineInstanceNodeListByInstanceId(Long instanceId);

    void finishPipelineInstanceNodeByArgs(Long instanceId, String nodeCode, PipelineStatusEnum pipelineStatusEnum, String desc, Date date);
}
