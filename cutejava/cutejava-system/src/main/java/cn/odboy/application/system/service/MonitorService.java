package cn.odboy.application.system.service;

import java.util.Map;

public interface MonitorService {

    /**
     * 查询数据分页
     *
     * @return Map<String, Object>
     */
    Map<String, Object> getServerInfo();
}
