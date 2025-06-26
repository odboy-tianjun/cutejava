package cn.odboy.devops.framework.pipeline.model;

import cn.odboy.base.CsObject;
import lombok.Getter;
import lombok.Setter;

/**
 * 流水线节点控制按钮
 *
 * @author odboy
 */
@Getter
@Setter
public class PipelineNodeTemplateButtonVo extends CsObject {
    /**
     * 按钮类型（service:调用某个服务 link:带参跳转）
     */
    private String type;
    /**
     * 按钮标题
     */
    private String title;
    /**
     * 出现的条件，根据node节点的状态判断
     */
    private String condition;
    /**
     * link: 按钮跳转的url
     */
    private String linkUrl;
    /**
     * link: 是否新窗口打开
     */
    private boolean isBlank = true;
    /**
     * service: 服务编码
     */
    private String code;
}