package cn.odboy.framework.monitor.service;

import org.springframework.http.ResponseEntity;

public interface CsHealthCheckPointService {
    /**
     * 就绪检查
     */
    ResponseEntity<?> doReadiness();

    /**
     * 存活检查
     */
    ResponseEntity<?> doLiveness();
}
