package cn.odboy.core.service.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemLocalStorageTb;
import cn.odboy.core.dal.model.system.QuerySystemLocalStorageArgs;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface SystemLocalStorageApi {

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    CsResultVo<List<SystemLocalStorageTb>> describeLocalStoragePage(QuerySystemLocalStorageArgs criteria, Page<SystemLocalStorageTb> page);

    /**
     * 查询全部数据
     *
     * @param criteria 条件
     * @return /
     */
    List<SystemLocalStorageTb> describeLocalStorageList(QuerySystemLocalStorageArgs criteria);
}
