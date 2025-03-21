package cn.odboy.modules.system.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;


@Data
@NoArgsConstructor
public class JobQueryCriteria {
    @ApiModelProperty(value = "岗位名称")
    private String name;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @ApiModelProperty(value = "创建时间")
    private List<Timestamp> createTime;
}