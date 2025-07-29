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

import cn.odboy.base.CsObject;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流水线节点模板
 *
 * @author odboy
 */
@Getter
@Setter
public class PipelineNodeTemplateVo extends CsObject {
    /**
     * 业务编码
     */
    protected String code;
    /**
     * 业务类型（service:系统内置服务 rpc:远程调用）
     */
    protected String type;
    /**
     * 业务名称
     */
    protected String name;
    /**
     * 是否可点击
     */
    protected Boolean click = false;
    /**
     * 是否可重试
     */
    protected Boolean retry = false;
    /**
     * 是否可点击：点击展示详情，详情内容类型
     */
    protected String detailType = "";
    /**
     * 默认参数<br/>
     * execute: 执行调用
     * describe: 明细调用
     */
    protected Map<String, Object> parameters = new HashMap<>();
    /**
     * 流水线节点控制按钮
     */
    protected List<PipelineNodeOperateButtonVo> buttons = new ArrayList<>();
}
