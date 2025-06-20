package cn.odboy.core.service.system;

import cn.odboy.core.dal.dataobject.system.SystemQiniuConfigTb;
import cn.odboy.core.dal.mysql.system.SystemQiniuConfigMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "qiNiu")
public class SystemQiniuConfigApiImpl implements SystemQiniuConfigApi {
    private final SystemQiniuConfigMapper qiniuConfigMapper;

    @Override
    @Cacheable(key = "'config'")
    public SystemQiniuConfigTb describeQiniuConfig() {
        SystemQiniuConfigTb qiniuConfig = qiniuConfigMapper.selectById(1L);
        return qiniuConfig == null ? new SystemQiniuConfigTb() : qiniuConfig;
    }
}
