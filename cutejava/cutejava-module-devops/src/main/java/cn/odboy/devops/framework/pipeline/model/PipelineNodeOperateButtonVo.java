package cn.odboy.devops.framework.pipeline.model;

import cn.odboy.base.CsObject;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 流水线节点控制按钮
 *
 * @author odboy
 */
@Getter
@Setter
public class PipelineNodeOperateButtonVo extends CsObject {
    /**
     * 按钮类型（service:调用某个服务并传递参数 link:带参跳转）
     */
    private String type;
    /**
     * 按钮标题
     */
    private String title;
    /**
     * 按钮传递的参数
     */
    private String code;
    /**
     * 默认附加参数
     */
    private Map<String, String> parameters = new HashMap<>();
}