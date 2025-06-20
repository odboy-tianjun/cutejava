package cn.odboy.core.service.system;

import cn.odboy.core.dal.dataobject.system.SystemQiniuConfigTb;

public interface SystemQiniuConfigApi {

    /**
     * 查询配置
     *
     * @return QiniuConfig
     */
    SystemQiniuConfigTb describeQiniuConfig();
}
