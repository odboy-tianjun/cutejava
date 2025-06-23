package cn.odboy.core.service.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemDictDetailTb;
import cn.odboy.core.dal.model.system.CreateSystemDictDetailArgs;
import cn.odboy.core.dal.model.system.QuerySystemDictDetailArgs;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


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

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    CsResultVo<List<SystemDictDetailTb>> queryDictDetailListByArgs(QuerySystemDictDetailArgs criteria, Page<Object> page);

    /**
     * 根据字典名称获取字典详情
     *
     * @param name 字典名称
     * @return /
     */
    List<SystemDictDetailTb> queryDictDetailListByName(String name);
}
