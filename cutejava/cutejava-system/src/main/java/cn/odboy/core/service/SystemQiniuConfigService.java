package cn.odboy.core.service;

import cn.odboy.core.constant.TransferProtocolConst;
import cn.odboy.core.dal.dataobject.SystemQiniuConfigTb;
import cn.odboy.core.dal.mysql.SystemQiniuConfigMapper;
import cn.odboy.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class SystemQiniuConfigService {
    private final SystemQiniuConfigMapper systemQiniuConfigMapper;

    /**
     * 保存
     *
     * @param qiniuConfig /
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveQiniuConfig(SystemQiniuConfigTb qiniuConfig) {
        qiniuConfig.setId(1L);
        if (!(qiniuConfig.getHost().toLowerCase().startsWith(TransferProtocolConst.PREFIX_HTTP) || qiniuConfig.getHost().toLowerCase().startsWith(TransferProtocolConst.PREFIX_HTTPS))) {
            throw new BadRequestException(TransferProtocolConst.PREFIX_HTTPS_BAD_REQUEST);
        }
        systemQiniuConfigMapper.insertOrUpdate(qiniuConfig);
    }

    /**
     * 更新
     *
     * @param type 类型
     */
    @Transactional(rollbackFor = Exception.class)
    public void modifyQiniuConfigType(String type) {
        SystemQiniuConfigTb qiniuConfig = systemQiniuConfigMapper.selectById(1L);
        qiniuConfig.setType(type);
        systemQiniuConfigMapper.insertOrUpdate(qiniuConfig);
    }

    /**
     * 查询配置
     *
     * @return /
     */
    public SystemQiniuConfigTb getLastQiniuConfig() {
        SystemQiniuConfigTb qiniuConfig = systemQiniuConfigMapper.selectById(1L);
        return qiniuConfig == null ? new SystemQiniuConfigTb() : qiniuConfig;
    }
}
