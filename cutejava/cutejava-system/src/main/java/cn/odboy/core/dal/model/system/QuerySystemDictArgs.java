package cn.odboy.core.dal.model.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QuerySystemDictArgs {

    @ApiModelProperty(value = "模糊查询")
    private String blurry;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer page = 1;

    @ApiModelProperty(value = "每页数据量", example = "10")
    private Integer size = 10;
}
