package cn.odboy.system.dal.model;

import cn.odboy.base.CsObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemProductLineVo extends CsObject {
    /**
     * 部门ID（select的value）
     */
    private Long value;
    /**
     * 产品线，如"研发部/华南分部/devops组"
     */
    private String label;
    /**
     * ID路径，如"1-2-3"
     */
    private String idPath;
}
