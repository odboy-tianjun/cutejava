package cn.odboy.service.impl;

import cn.odboy.exception.BadRequestException;
import cn.odboy.mapper.QiniuConfigMapper;
import cn.odboy.model.tools.domain.QiniuConfig;
import cn.odboy.service.QiNiuConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "qiNiu")
public class QiNiuConfigServiceImpl extends ServiceImpl<QiniuConfigMapper, QiniuConfig> implements QiNiuConfigService {

    @Override
    @Cacheable(key = "'config'")
    public QiniuConfig getConfig() {
        QiniuConfig qiniuConfig = getById(1L);
        return qiniuConfig == null ? new QiniuConfig() : qiniuConfig;
    }

    @Override
    @CacheEvict(key = "'config'")
    @Transactional(rollbackFor = Exception.class)
    public void saveConfig(QiniuConfig qiniuConfig) {
        qiniuConfig.setId(1L);
        String http = "http://", https = "https://";
        if (!(qiniuConfig.getHost().toLowerCase().startsWith(http) || qiniuConfig.getHost().toLowerCase().startsWith(https))) {
            throw new BadRequestException("外链域名必须以http://或者https://开头");
        }
        saveOrUpdate(qiniuConfig);
    }

    @Override
    @CacheEvict(key = "'config'")
    @Transactional(rollbackFor = Exception.class)
    public void updateType(String type) {
        QiniuConfig qiniuConfig = getById(1L);
        qiniuConfig.setType(type);
        saveOrUpdate(qiniuConfig);
    }
}
