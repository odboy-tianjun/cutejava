package cn.odboy.core.dal.model.system;

import cn.odboy.base.CsSerializeObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreateSystemDeptArgs extends CsSerializeObject {
    @NotBlank(message = "部门名称必填")
    private String name;
    @NotNull(message = "部门排序必填")
    private Integer deptSort;
    private Boolean enabled;
    private String isTop;
    private Long pid;
    private Integer subCount = 0;
}
