package cn.odboy.devops.constant.pipeline;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流水线状态枚举
 */
@Getter
@AllArgsConstructor
public enum PipelineStatusEnum {
    PENDING("pending", "#C0C4CC", "未开始"),
    RUNNING("running", "#409EFF", "运行中"),
    SUCCESS("success", "#67C23A", "执行成功"),
    FAIL("fail", "#F56C6C", "执行失败");
    /**
     * 状态码
     */
    private final String code;
    /**
     * 颜色
     */
    private final String color;
    /**
     * 描述
     */
    private final String desc;
}