package cn.odboy.devops.framework.pipeline.model;

import cn.odboy.base.CsObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 流水线实例Vo
 *
 * @author odboy
 */
@Getter
@Setter
public class PipelineInstanceVo extends CsObject {
    private Long instanceId;
    private String instanceName;
    private String env;
    private String contextName;
    private String contextParams;
    private List<PipelineNodeDataVo> nodes;
}
