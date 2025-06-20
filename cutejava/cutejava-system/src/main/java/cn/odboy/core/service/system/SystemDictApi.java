package cn.odboy.core.service.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemDictTb;
import cn.odboy.core.dal.model.system.QuerySystemDictArgs;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface SystemDictApi {
    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    CsResultVo<List<SystemDictTb>> describeDictPage(QuerySystemDictArgs criteria, Page<SystemDictTb> page);

    /**
     * 查询全部数据
     *
     * @param criteria /
     * @return /
     */
    List<SystemDictTb> describeDictList(QuerySystemDictArgs criteria);
}
