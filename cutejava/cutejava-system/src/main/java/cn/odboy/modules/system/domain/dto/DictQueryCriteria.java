package cn.odboy.modules.system.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DictQueryCriteria {
    @ApiModelProperty(value = "模糊查询")
    private String blurry;
}
