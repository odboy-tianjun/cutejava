package cn.odboy.system.dal.model;

import cn.odboy.base.CsObject;
import cn.odboy.system.dal.dataobject.SystemDeptTb;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
public class SystemCreateRoleArgs extends CsObject {
    @NotBlank(message = "角色名称必填")
    private String name;
    @NotNull(message = "角色级别必填")
    private Integer level;
    @NotBlank(message = "数据范围必填")
    private String dataScope;
    private String description;
    /**
     * 关联的部门
     */
    private Long id;
    private Set<SystemDeptTb> depts;
}
