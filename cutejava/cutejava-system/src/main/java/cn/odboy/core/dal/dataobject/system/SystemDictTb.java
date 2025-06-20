package cn.odboy.core.dal.dataobject.system;

import cn.odboy.base.CsBaseUserTimeTb;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@TableName("system_dict")
public class SystemDictTb extends CsBaseUserTimeTb {

    @NotNull(groups = Update.class)
    @ApiModelProperty(value = "ID", hidden = true)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "描述")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String description;
}