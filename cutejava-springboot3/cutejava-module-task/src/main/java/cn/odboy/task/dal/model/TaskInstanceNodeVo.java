/*
 * Copyright 2021-2025 Odboy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.odboy.task.dal.model;

import cn.odboy.base.CsObject;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 任务节点数据
 *
 * @author odboy
 */
@Getter
@Setter
public class TaskInstanceNodeVo extends CsObject {
    /**
     * 业务编码
     */
    protected String code;
    /**
     * 业务名称
     */
    protected String name;
    /**
     * 开始时间
     */
    protected Date startTime;
    /**
     * 完成时间
     */
    protected Date finishTime;
    /**
     * 耗时多久
     */
    protected String durationDesc;
    /**
     * 进行中描述
     */
    protected String runningDesc;
    /**
     * 节点状态编码
     */
    protected String status;
    /**
     * 节点状态描述
     */
    protected String statusDesc;
}
