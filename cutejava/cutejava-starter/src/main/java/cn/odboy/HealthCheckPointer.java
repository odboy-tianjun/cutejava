package cn.odboy;

import cn.odboy.framework.monitor.service.CsHealthCheckPointService;
import cn.odboy.system.service.SystemDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class HealthCheckPointer implements CsHealthCheckPointService {
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
