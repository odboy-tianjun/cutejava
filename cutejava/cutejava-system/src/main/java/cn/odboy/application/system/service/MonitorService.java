package cn.odboy.application.system.service;

import java.util.Map;

public interface MonitorService {

    /**
     * 查询服务器监控信息
     *
     * @return Map<String, Object>
     */
    Map<String, Object> describeServerMonitorInfo();
}
