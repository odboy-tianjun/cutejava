package cn.odboy.core.service.system;

import cn.odboy.core.dal.dataobject.system.SystemDictDetailTb;
import cn.odboy.core.dal.model.system.CreateSystemDictDetailArgs;
import com.baomidou.mybatisplus.extension.service.IService;


public interface SystemDictDetailService extends IService<SystemDictDetailTb> {

    /**
     * 创建
     *
     * @param resources /
     */
    void saveDictDetail(CreateSystemDictDetailArgs resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void modifyDictDetailById(SystemDictDetailTb resources);

    /**
     * 删除
     *
     * @param id /
     */
    void removeDictDetailById(Long id);
}