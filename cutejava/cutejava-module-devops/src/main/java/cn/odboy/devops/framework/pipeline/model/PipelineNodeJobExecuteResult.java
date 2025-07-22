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
    private Object data;

    public static PipelineNodeJobExecuteResult success() {
        PipelineNodeJobExecuteResult result = new PipelineNodeJobExecuteResult();
        result.setStatus(PipelineStatusEnum.SUCCESS);
        result.setMessage(PipelineStatusEnum.SUCCESS.getDesc());
        result.setData("");
        return result;
    }

    public static PipelineNodeJobExecuteResult success(Object data) {
        PipelineNodeJobExecuteResult result = new PipelineNodeJobExecuteResult();
        result.setStatus(PipelineStatusEnum.SUCCESS);
        result.setMessage(PipelineStatusEnum.SUCCESS.getDesc());
        result.setData(data);
        return result;
    }

    public static PipelineNodeJobExecuteResult success(String message, Object data) {
        PipelineNodeJobExecuteResult result = new PipelineNodeJobExecuteResult();
        result.setStatus(PipelineStatusEnum.SUCCESS);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static PipelineNodeJobExecuteResult fail() {
        PipelineNodeJobExecuteResult result = new PipelineNodeJobExecuteResult();
        result.setStatus(PipelineStatusEnum.FAIL);
        result.setMessage(PipelineStatusEnum.FAIL.getDesc());
        return result;
    }

    public static PipelineNodeJobExecuteResult fail(String message) {
        PipelineNodeJobExecuteResult result = new PipelineNodeJobExecuteResult();
        result.setStatus(PipelineStatusEnum.FAIL);
        result.setMessage(message);
        return result;
    }
}
