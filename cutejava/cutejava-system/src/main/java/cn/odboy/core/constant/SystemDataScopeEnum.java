package cn.odboy.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据权限枚举
 */
@Getter
@AllArgsConstructor
public enum SystemDataScopeEnum {

    /* 全部的数据权限 */
    ALL("全部", "全部的数据权限"),

    /* 自己部门的数据权限 */
    THIS_LEVEL("本级", "自己部门的数据权限"),

    /* 自定义的数据权限 */
    CUSTOMIZE("自定义", "自定义的数据权限");

    private final String value;
    private final String description;

    public static SystemDataScopeEnum find(String val) {
        for (SystemDataScopeEnum dataScopeEnum : SystemDataScopeEnum.values()) {
            if (dataScopeEnum.getValue().equals(val)) {
                return dataScopeEnum;
            }
        }
        return null;
    }

}
