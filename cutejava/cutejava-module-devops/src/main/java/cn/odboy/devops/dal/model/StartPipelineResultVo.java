package cn.odboy.devops.dal.model;

import cn.odboy.base.CsObject;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StartPipelineResultVo extends CsObject {
    /**
     * 流水线实例id
     */
    @JSONField(serializeFeatures = JSONWriter.Feature.WriteLongAsString)
    private Long instanceId;
    /**
     * 流水线模板内容
     */
    private String templateContent;
}
