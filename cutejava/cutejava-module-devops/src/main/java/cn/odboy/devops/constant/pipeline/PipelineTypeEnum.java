package cn.odboy.devops.constant.pipeline;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流水线类型
 */
@Getter
@AllArgsConstructor
public enum PipelineTypeEnum {
    BACKEND("backend", "后端"),
    FRONT("front", "前端"),
    MOBILE("mobile", "移动端"),
    ANDROID("pc", "PC端"),
    UN_SUPPORT("un_support", "不支持的类型");
    /**
     * 类型编码
     */
    private final String code;
    /**
     * 描述
     */
    private final String desc;

    public static PipelineTypeEnum getByCode(String code) {
        for (PipelineTypeEnum value : PipelineTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return UN_SUPPORT;
    }
}