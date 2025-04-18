package cn.odboy.monitor;

import cn.odboy.application.system.service.DictService;
import cn.odboy.monitor.service.HealthCheckPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class HealthCheckPointerImpl implements HealthCheckPointService {
    @Autowired
    private DictService dictService;

    @Override
    public ResponseEntity<?> doReadiness() {
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<?> doLiveness() {
        return ResponseEntity.ok(dictService.getById(1));
    }
}
