package cn.odboy.system.dal.dataobject;

import cn.odboy.base.CsObject;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * <p>
 * 系统审计日志
 * </p>
 *
 * @author codegen
 * @since 2025-05-12
 */
@Getter
@Setter
@ToString
@TableName("system_operation_log")
@ApiModel(value = "SystemOperationLog对象", description = "审计日志")
public class SystemOperationLogTb extends CsObject {

    /**
     * id
     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 业务名称
     */
    @TableField("biz_name")
    private String bizName;
    /**
     * 业务方法
     */
    @TableField("method")
    private String method;
    /**
     * 业务参数
     */
    @TableField("params")
    private String params;
    /**
     * 访问来源IP
     */
    @TableField("request_ip")
    private String requestIp;
    /**
     * 执行时间
     */
    @TableField("execute_time")
    private Long executeTime;
    /**
     * 用户名
     */
    @TableField("username")
    private String username;
    /**
     * 访问来源地址
     */
    @TableField("address")
    private String address;
    /**
     * 浏览器信息
     */
    @TableField("browser_info")
    private String browserInfo;
    /**
     * 异常详情
     */
    @TableField("exception_detail")
    private String exceptionDetail;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
}
