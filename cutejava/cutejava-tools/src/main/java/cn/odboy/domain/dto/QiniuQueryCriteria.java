package cn.odboy.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;


@Data
public class QiniuQueryCriteria {
    @ApiModelProperty(value = "名称查询")
    private String key;

    @ApiModelProperty(value = "创建时间")
    private List<Timestamp> createTime;
}
