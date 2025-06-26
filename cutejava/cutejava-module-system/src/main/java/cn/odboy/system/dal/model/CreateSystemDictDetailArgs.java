package cn.odboy.system.dal.model;

import cn.odboy.base.CsObject;
import cn.odboy.system.dal.dataobject.SystemDictTb;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreateSystemDictDetailArgs extends CsObject {
    @NotNull(message = "参数dict必填")
    private SystemDictTb dict;
    @NotBlank(message = "字典标签必填")
    private String label;
    @NotBlank(message = "字典值必填")
    private String value;
    @NotNull(message = "字典排序必填")
    private Integer dictSort;
}
