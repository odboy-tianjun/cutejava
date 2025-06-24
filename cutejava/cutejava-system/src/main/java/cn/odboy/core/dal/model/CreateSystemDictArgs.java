package cn.odboy.core.dal.model;

import cn.odboy.base.CsSerializeObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreateSystemDictArgs extends CsSerializeObject {
    @NotBlank(message = "字典名称必填")
    private String name;
    private String description;
}
