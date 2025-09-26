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
 * 任务实例明细
 * </p>
 *
 * @author codegen
 * @since 2025-09-26
 */
@Getter
@Setter
@ToString
@TableName("task_instance_detail")
@ApiModel(value = "TaskInstanceDetailTb对象", description = "任务实例明细")
public class TaskInstanceDetailTb extends CsBaseUserTimeTb {

    /**
     * id
     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 任务实例id
     */
    @TableField("instance_id")
    @ApiModelProperty("任务实例id")
    private String instanceId;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    @TableField("finish_time")
    private Date finishTime;

    /**
     * 业务编码
     */
    @TableField("biz_code")
    @ApiModelProperty("业务编码")
    private String bizCode;

    /**
     * 业务名称(步骤)
     */
    @TableField("biz_name")
    @ApiModelProperty("业务名称(步骤)")
    private String bizName;

    /**
     * 执行信息
     */
    @ApiModelProperty("执行信息")
    @TableField("execute_info")
    private String executeInfo;

    /**
     * 执行状态(running进行中 success成功 fail失败)
     */
    @TableField("execute_status")
    @ApiModelProperty("执行状态(running进行中 success成功 fail失败)")
    private String executeStatus;
}
