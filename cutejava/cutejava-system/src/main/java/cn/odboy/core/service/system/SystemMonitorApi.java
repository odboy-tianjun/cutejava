package cn.odboy.core.service.system;

import java.util.Map;

public interface SystemMonitorApi {

    /**
     * 查询服务器监控信息
     *
     * @return Map<String, Object>
     */
    Map<String, Object> describeServerMonitorInfo();
}
