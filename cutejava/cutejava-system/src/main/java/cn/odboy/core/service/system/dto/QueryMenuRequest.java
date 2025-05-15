package cn.odboy.core.service.system.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import java.util.List;


@Data
public class QueryMenuRequest {

    @ApiModelProperty(value = "模糊查询")
    private String blurry;

    @ApiModelProperty(value = "创建时间")
    private List<Date> createTime;

    @ApiModelProperty(value = "PID为空查询")
    private Boolean pidIsNull;

    @ApiModelProperty(value = "PID")
    private Long pid;
}
