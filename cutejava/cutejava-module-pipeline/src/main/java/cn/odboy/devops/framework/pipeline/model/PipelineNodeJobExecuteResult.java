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
import cn.odboy.devops.framework.pipeline.constant.PipelineStatusEnum;
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
