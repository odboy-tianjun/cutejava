package cn.odboy.system.dal.model;

import cn.odboy.base.CsObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreateSystemDictArgs extends CsObject {
    @NotBlank(message = "字典名称必填")
    private String name;
    private String description;
}
