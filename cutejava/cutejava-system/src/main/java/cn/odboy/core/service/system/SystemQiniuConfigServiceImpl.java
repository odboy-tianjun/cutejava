package cn.odboy.core.service.system;

import cn.odboy.core.constant.TransferProtocolConst;
import cn.odboy.core.dal.dataobject.system.SystemQiniuConfigTb;
import cn.odboy.core.dal.mysql.system.SystemQiniuConfigMapper;
import cn.odboy.exception.BadRequestException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "qiNiu")
public class SystemQiniuConfigServiceImpl extends ServiceImpl<SystemQiniuConfigMapper, SystemQiniuConfigTb> implements SystemQiniuConfigService {


    @Override
    @CacheEvict(key = "'config'")
    @Transactional(rollbackFor = Exception.class)
    public void saveQiniuConfig(SystemQiniuConfigTb qiniuConfig) {
        qiniuConfig.setId(1L);
        if (!(qiniuConfig.getHost().toLowerCase().startsWith(TransferProtocolConst.PREFIX_HTTP) || qiniuConfig.getHost().toLowerCase().startsWith(TransferProtocolConst.PREFIX_HTTPS))) {
            throw new BadRequestException(TransferProtocolConst.PREFIX_HTTPS_BAD_REQUEST);
        }
        saveOrUpdate(qiniuConfig);
    }

    @Override
    @CacheEvict(key = "'config'")
    @Transactional(rollbackFor = Exception.class)
    public void modifyQiniuConfigType(String type) {
        SystemQiniuConfigTb qiniuConfig = getById(1L);
        qiniuConfig.setType(type);
        saveOrUpdate(qiniuConfig);
    }
}
