package cn.odboy.system.dal.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色菜单
 */
@Getter
@Setter
@TableName("system_users_jobs")
public class SystemUserJobTb {
    @TableField(value = "user_id") private Long userId;
    @TableField(value = "job_id") private Long jobId;
}
