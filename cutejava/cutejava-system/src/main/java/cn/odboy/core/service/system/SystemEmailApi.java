package cn.odboy.core.service.system;

import cn.odboy.core.dal.dataobject.system.SystemEmailConfigTb;

public interface SystemEmailApi {

    /**
     * 查询配置
     *
     * @return EmailConfig 邮件配置
     */
    SystemEmailConfigTb describeEmailConfig();
}
