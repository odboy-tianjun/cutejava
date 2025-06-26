package cn.odboy.devops.framework.pipeline.model;

import cn.odboy.base.CsObject;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流水线节点模板
 *
 * @author odboy
 */
@Getter
@Setter
public class PipelineNodeTemplateVo extends CsObject {
    /**
     * 业务编码
     */
    private String code;
    /**
     * 业务名称
     */
    private String name;
    /**
     * 是否可点击
     */
    private Boolean click;
    /**
     * 是否可重试
     */
    private Boolean retry;
    /**
     * 是否可点击：点击展示详情，详情内容类型
     */
    private String detailType;
    /**
     * 默认参数
     */
    private Map<String, String> parameters = new HashMap<>();
    /**
     * 流水线节点控制按钮
     */
    private List<PipelineNodeTemplateButtonVo> buttons = new ArrayList<>();
}
