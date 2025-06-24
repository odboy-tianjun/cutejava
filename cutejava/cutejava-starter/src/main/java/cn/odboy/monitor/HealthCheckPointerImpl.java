package cn.odboy.monitor;

import cn.odboy.core.service.SystemDictService;
import cn.odboy.monitor.service.CsHealthCheckPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class HealthCheckPointerImpl implements CsHealthCheckPointService {
    @Autowired
    private SystemDictService systemDictService;

    @Override
    public ResponseEntity<?> doReadiness() {
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<?> doLiveness() {
        return ResponseEntity.ok(systemDictService.getById(1));
    }
}
