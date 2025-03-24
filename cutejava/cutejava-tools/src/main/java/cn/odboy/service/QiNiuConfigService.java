package cn.odboy.service;

import cn.odboy.model.tools.domain.QiniuConfig;
import com.baomidou.mybatisplus.extension.service.IService;


public interface QiNiuConfigService extends IService<QiniuConfig> {

    /**
     * 查询配置
     *
     * @return QiniuConfig
     */
    QiniuConfig getConfig();

    /**
     * 保存
     *
     * @param type 类型
     */
    void saveConfig(QiniuConfig type);

    /**
     * 更新
     *
     * @param type 类型
     */
    void updateType(String type);
}
