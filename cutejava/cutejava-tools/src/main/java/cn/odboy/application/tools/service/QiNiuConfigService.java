package cn.odboy.application.tools.service;

import cn.odboy.model.tools.domain.QiniuConfig;
import com.baomidou.mybatisplus.extension.service.IService;


public interface QiNiuConfigService extends IService<QiniuConfig> {

    /**
     * 查询配置
     *
     * @return QiniuConfig
     */
    QiniuConfig describeQiniuConfig();

    /**
     * 保存
     *
     * @param type 类型
     */
    void saveQiniuConfig(QiniuConfig type);

    /**
     * 更新
     *
     * @param type 类型
     */
    void modifyQiniuConfigType(String type);
}
