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
package cn.odboy.devops.framework.pipeline.model;

import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeDetailTb;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 流水线节点数据
 *
 * @author odboy
 */
@Getter
@Setter
public class PipelineInstanceNodeVo extends PipelineNodeTemplateVo {
    /**
     * 节点创建时间
     */
    private Date createTime;
    /**
     * 节点更新时间
     */
    private Date updateTime;
    /**
     * 节点开始运行时间
     */
    private Date startTime;
    /**
     * 节点结束运行时间
     */
    private Date finishTime;
    /**
     * 流水线节点步骤信息
     */
    private String currentNodeMsg;
    /**
     * 流水线节点状态
     */
    private String currentNodeStatus;
    /**
     * 流水线实例状态
     */
    private String status;
    /**
     * 节点步骤明细记录
     */
    private List<PipelineInstanceNodeDetailTb> historys = new ArrayList<>();
}
