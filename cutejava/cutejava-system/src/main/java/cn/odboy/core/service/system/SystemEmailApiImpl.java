package cn.odboy.core.service.system;

import cn.odboy.core.dal.dataobject.system.SystemEmailConfigTb;
import cn.odboy.core.dal.mysql.system.SystemEmailConfigMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "email")
public class SystemEmailApiImpl implements SystemEmailApi {
    private final SystemEmailConfigMapper emailConfigMapper;

    @Override
    @Cacheable(key = "'config'")
    public SystemEmailConfigTb describeEmailConfig() {
        SystemEmailConfigTb emailConfig = emailConfigMapper.selectById(1L);
        return emailConfig == null ? new SystemEmailConfigTb() : emailConfig;
    }
}
