package cn.odboy.devops.framework.pipeline.model;

import cn.odboy.base.CsObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
    private String instanceId;
    private String instanceName;
    private String env;
    private String code;
    private String contextName;
    private String contextParams;
    private List<PipelineInstanceNodeVo> nodes;
}
