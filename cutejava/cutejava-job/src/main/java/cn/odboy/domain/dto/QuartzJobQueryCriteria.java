package cn.odboy.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;


@Data
public class QuartzJobQueryCriteria {
    @ApiModelProperty(value = "定时任务名称")
    private String jobName;

    @ApiModelProperty(value = "是否成功")
    private Boolean isSuccess;

    @ApiModelProperty(value = "创建时间")
    private List<Timestamp> createTime;
}
