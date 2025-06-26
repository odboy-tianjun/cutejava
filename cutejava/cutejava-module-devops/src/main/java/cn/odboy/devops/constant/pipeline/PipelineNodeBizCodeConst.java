package cn.odboy.devops.constant.pipeline;

/**
 * 流水线节点业务编码
 *
 * @author odboy
 */
public interface PipelineNodeBizCodeConst {
    /**
     * 初始化
     */
    String INIT = PipelineConst.EXECUTOR_PREFIX + "node_init";

    /**
     * 合并代码
     */
    String MERGE_BRANCH = PipelineConst.EXECUTOR_PREFIX + "node_merge_branch";

    /**
     * 镜像扫描
     */
    String IMAGE_SCAN = PipelineConst.EXECUTOR_PREFIX + "node_image_scan";

    /**
     * 部署审批
     */
    String DEPLOY_APPROVE = PipelineConst.EXECUTOR_PREFIX + "node_deploy_approve";

    /**
     * 合并到Master
     */
    String MERGE_MASTER = PipelineConst.EXECUTOR_PREFIX + "node_merge_master";

    interface AppJava {
        /**
         * 构建
         */
        String BUILD = PipelineConst.EXECUTOR_PREFIX + "node_build_java";
        /**
         * 部署
         */
        String DEPLOY = PipelineConst.EXECUTOR_PREFIX + "node_deploy_java";
    }

    interface AppPython {
        /**
         * 构建
         */
        String BUILD = PipelineConst.EXECUTOR_PREFIX + "node_build_python";
        /**
         * 部署
         */
        String DEPLOY = PipelineConst.EXECUTOR_PREFIX + "node_deploy_python";
    }

    interface AppGo {
        /**
         * 构建
         */
        String BUILD = PipelineConst.EXECUTOR_PREFIX + "node_build_go";
        /**
         * 部署
         */
        String DEPLOY = PipelineConst.EXECUTOR_PREFIX + "node_deploy_go";
    }

    interface AppNodeJs {
        /**
         * 构建
         */
        String BUILD = PipelineConst.EXECUTOR_PREFIX + "node_build_nodejs";
        /**
         * 部署
         */
        String DEPLOY = PipelineConst.EXECUTOR_PREFIX + "node_deploy_nodejs";
    }
}
