package cn.odboy.devops.constant.pipeline;

/**
 * 流水线 常量
 */
public interface PipelineConst {
    /**
     * 执行器服务前缀
     */
    String EXECUTOR_PREFIX = "pipeline:executor:";
    /**
     * 流水线实例Key
     */
    String INSTANCE_ID_KEY = "pipelineInstanceId";
    String INSTANCE_KEY = "pipelineInstance";
    /**
     * 流水线实例节点相关Key
     */
    String INSTANCE_NODE_INDEX_KEY = "pipelineInstanceNodeIndex";
    String INSTANCE_NODE_CODE_KEY = "pipelineInstanceNodeCode";
    String INSTANCE_NODE_NAME_KEY = "pipelineInstanceNodeName";
    String INSTANCE_NODE_TEMPLATE_ARGS_KEY = "pipelineInstanceNodeTemplateArgs";
}
