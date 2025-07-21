package cn.odboy.devops.dal.dataobject;

import cn.odboy.base.CsBaseUserTimeLogicTb;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 流水线模板
 * </p>
 *
 * @author codegen
 * @since 2025-06-26
 */
@Getter
@Setter
@TableName("pipeline_template")
@ApiModel(value = "PipelineTemplate对象", description = "流水线模板")
public class PipelineTemplateTb extends CsBaseUserTimeLogicTb {

    /**
     * id
     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 流水线类型
     */
    @TableField("`type`")
    @ApiModelProperty("流水线类型")
    private String type;

    /**
     * 语言
     */
    @ApiModelProperty("语言")
    @TableField("`language`")
    private String language;

    /**
     * 环境编码
     */
    @TableField("env")
    @ApiModelProperty("环境编码")
    private String env;

    /**
     * 流水线编码
     */
    @TableField("`code`")
    @ApiModelProperty("流水线编码")
    private String code;

    /**
     * 流水线名称
     */
    @TableField("`name`")
    @ApiModelProperty("流水线名称")
    private String name;

    /**
     * 流水线描述
     */
    @ApiModelProperty("流水线描述")
    @TableField("`description`")
    private String description;

    /**
     * 流水线模板内容
     */
    @TableField("template")
    @ApiModelProperty("流水线模板内容")
    private String template;
}
