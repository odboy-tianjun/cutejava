package cn.odboy.devops.dal.model;

import cn.odboy.base.CsObject;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class DevOpsQueryLastPipelineDetailArgs extends CsObject {
    @NotBlank(message = "流水线实例Id必填")
    private String instanceId;
}
