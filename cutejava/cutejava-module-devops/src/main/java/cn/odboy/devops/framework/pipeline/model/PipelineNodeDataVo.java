package cn.odboy.devops.framework.pipeline.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 流水线节点数据
 *
 * @author odboy
 */
@Getter
@Setter
public class PipelineNodeDataVo extends PipelineNodeTemplateVo {
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
}
