/*
 * Copyright 2021-2025 Odboy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.odboy.system.dal.dataobject;

import cn.odboy.base.CsObject;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "SystemOperationLog对象", description = "审计日志")
public class SystemOperationLogTb extends CsObject {

    /**
     * id
     */
    @Schema(name = "id")
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
