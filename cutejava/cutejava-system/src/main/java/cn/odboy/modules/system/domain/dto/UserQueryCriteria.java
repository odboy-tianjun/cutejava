package cn.odboy.modules.system.domain.dto;

import cn.odboy.base.MyObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserQueryCriteria extends MyObject {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "多个ID")
    private Set<Long> deptIds = new HashSet<>();

    @ApiModelProperty(value = "模糊查询")
    private String blurry;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "创建时间")
    private List<Timestamp> createTime;
}
