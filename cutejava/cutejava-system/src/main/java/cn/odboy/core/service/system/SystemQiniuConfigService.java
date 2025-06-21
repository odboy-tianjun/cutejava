package cn.odboy.core.service.system;

import cn.odboy.core.dal.dataobject.system.SystemQiniuConfigTb;
import com.baomidou.mybatisplus.extension.service.IService;


public interface SystemQiniuConfigService extends IService<SystemQiniuConfigTb> {

    /**
     * 保存
     *
     * @param type 类型
     */
    void saveQiniuConfig(SystemQiniuConfigTb type);

    /**
     * 更新
     *
     * @param type 类型
     */
    void modifyQiniuConfigType(String type);

    /**
     * 查询配置
     *
     * @return QiniuConfig
     */
    SystemQiniuConfigTb describeQiniuConfig();
}
