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
    protected String code;
    /**
     * 业务类型（service:系统内置服务 rpc:远程调用）
     */
    protected String type;
    /**
     * 业务名称
     */
    protected String name;
    /**
     * 是否可点击
     */
    protected Boolean click = false;
    /**
     * 是否可重试
     */
    protected Boolean retry = false;
    /**
     * 是否可点击：点击展示详情，详情内容类型
     */
    protected String detailType = "";
    /**
     * 默认参数
     */
    protected Map<String, Object> parameters = new HashMap<>();
    /**
     * 流水线节点控制按钮
     */
    protected List<PipelineNodeOperateButtonVo> buttons = new ArrayList<>();
}
