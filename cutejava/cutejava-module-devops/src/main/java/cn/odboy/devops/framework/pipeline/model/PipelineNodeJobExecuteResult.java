package cn.odboy.devops.framework.pipeline.model;

import cn.odboy.base.CsObject;
import cn.odboy.devops.constant.pipeline.PipelineStatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PipelineNodeJobExecuteResult extends CsObject {
    private PipelineStatusEnum status;
    private String message;

    public static PipelineNodeJobExecuteResult success() {
        PipelineNodeJobExecuteResult result = new PipelineNodeJobExecuteResult();
        result.setStatus(PipelineStatusEnum.SUCCESS);
        result.setMessage(PipelineStatusEnum.SUCCESS.getDesc());
        return result;
    }

    public static PipelineNodeJobExecuteResult fail() {
        PipelineNodeJobExecuteResult result = new PipelineNodeJobExecuteResult();
        result.setStatus(PipelineStatusEnum.FAIL);
        result.setMessage(PipelineStatusEnum.FAIL.getDesc());
        return result;
    }
}
