package cn.odboy.system.dal.model;

import cn.odboy.base.CsObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreateSystemJobArgs extends CsObject {
    @NotBlank(message = "职位名称必填")
    private String name;
    @NotNull(message = "职位排序必填")
    private Integer jobSort;
    private Boolean enabled;
}
