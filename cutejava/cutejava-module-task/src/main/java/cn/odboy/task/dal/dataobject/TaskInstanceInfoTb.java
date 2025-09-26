package cn.odboy.task.dal.dataobject;

import cn.odboy.base.CsBaseUserTimeTb;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * <p>
 * 任务实例
 * </p>
 *
 * @author codegen
 * @since 2025-09-26
 */
@Getter
@Setter
@ToString
@TableName("task_instance_info")
@ApiModel(value = "TaskInstanceInfoTb对象", description = "任务实例")
public class TaskInstanceInfoTb extends CsBaseUserTimeTb {

    /**
     * id
     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    @TableField("context_name")
    private String contextName;

    /**
     * 变更类型
     */
    @ApiModelProperty("变更类型")
    @TableField("change_type")
    private String changeType;

    /**
     * 环境别名
     */
    @TableField("env_alias")
    @ApiModelProperty("环境别名")
    private String envAlias;

    /**
     * 状态(running进行中 success成功 fail失败)
     */
    @TableField("`status`")
    @ApiModelProperty("状态(running进行中 success成功 fail失败)")
    private String status;

    /**
     * 完成时间
     */
    @ApiModelProperty("完成时间")
    @TableField("finish_time")
    private Date finishTime;

    /**
     * 来源
     */
    @ApiModelProperty("来源")
    @TableField("`source`")
    private String source;

    /**
     * 变更原因
     */
    @TableField("reason")
    @ApiModelProperty("变更原因")
    private String reason;

    /**
     * QuartzJob的key名称
     */
    @TableField("bind_job_key")
    @ApiModelProperty("QuartzJob的key名称")
    private String bindJobKey;

    /**
     * QuartzJob参数
     */
    @TableField("bind_job_data")
    @ApiModelProperty("QuartzJob参数")
    private String bindJobData;
}
