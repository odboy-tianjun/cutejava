package cn.odboy.application.tools.service.impl;

import cn.odboy.application.tools.mapper.QiniuConfigMapper;
import cn.odboy.application.tools.service.QiNiuConfigService;
import cn.odboy.constant.TransferProtocolConst;
import cn.odboy.exception.BadRequestException;
import cn.odboy.model.tools.domain.QiniuConfig;
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
        if (!(qiniuConfig.getHost().toLowerCase().startsWith(TransferProtocolConst.PREFIX_HTTP) || qiniuConfig.getHost().toLowerCase().startsWith(TransferProtocolConst.PREFIX_HTTPS))) {
            throw new BadRequestException(TransferProtocolConst.PREFIX_HTTPS_BAD_REQUEST);
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
