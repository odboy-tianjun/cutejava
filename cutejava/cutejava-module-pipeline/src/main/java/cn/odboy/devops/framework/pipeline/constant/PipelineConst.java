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
package cn.odboy.devops.framework.pipeline.constant;

/**
 * 流水线 常量
 */
public interface PipelineConst {
    /**
     * 执行器服务前缀
     */
    String INSTANCE_PREFIX = "pipeline_instance_";
    String EXECUTOR_PREFIX = "pipeline_executor_";
    //    /**
    //     * 流水线实例Key
    //     */
    //    String INSTANCE_KEY = "pipelineInstance";
    //    /**
    //     * 流水线实例节点相关Key
    //     */
    //    String INSTANCE_NODE_INDEX_KEY = "pipelineInstanceNodeIndex";
    //    String INSTANCE_NODE_CODE_KEY = "pipelineInstanceNodeCode";
    //    String INSTANCE_NODE_NAME_KEY = "pipelineInstanceNodeName";
    //    String INSTANCE_NODE_TEMPLATE_ARGS_KEY = "pipelineInstanceNodeTemplateArgs";
    //    String INSTANCE_RETRY_NODE_INDEX_KEY = "pipelineInstanceRetryNodeIndex";
    // 以下为标准常量
    String INSTANCE = "Instance";
    String INSTANCE_ID = "InstanceId";
    String TEMPLATE = "Template";
    String TEMPLATE_ID = "TemplateId";
    String ENV = "Env";
    String CONTEXT_NAME = "ContextName";
    String RETRY_NODE_CODE = "RetryNodeCode";
    String CURRENT_NODE_TEMPLATE = "CurrentNodeTemplate";
    String LAST_NODE_RESULT = "LastNodeResult";
}
