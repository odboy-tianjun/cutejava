package cn.odboy.system.dal.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class SystemQueryQuartzJobArgs {

    @ApiModelProperty(value = "定时任务名称")
    private String jobName;

    @ApiModelProperty(value = "是否成功")
    private Boolean isSuccess;

    @ApiModelProperty(value = "创建时间")
    private List<Date> createTime;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer page = 1;

    @ApiModelProperty(value = "每页数据量", example = "10")
    private Integer size = 10;
}
