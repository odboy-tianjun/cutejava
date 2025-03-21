package cn.odboy.modules.system.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DictDetailQueryCriteria {
    @ApiModelProperty(value = "标签")
    private String label;

    @ApiModelProperty(value = "字典名称")
    private String dictName;
}