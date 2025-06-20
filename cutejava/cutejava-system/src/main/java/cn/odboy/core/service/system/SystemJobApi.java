package cn.odboy.core.service.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemJobTb;
import cn.odboy.core.dal.model.system.QuerySystemJobArgs;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Set;

public interface SystemJobApi {

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    CsResultVo<List<SystemJobTb>> describeJobPage(QuerySystemJobArgs criteria, Page<SystemJobTb> page);

    /**
     * 查询全部数据
     *
     * @param criteria /
     * @return /
     */
    List<SystemJobTb> describeJobList(QuerySystemJobArgs criteria);


    /**
     * 验证是否被用户关联
     *
     * @param ids /
     */
    void verifyBindRelationByIds(Set<Long> ids);

    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */
    SystemJobTb describeJobById(Long id);
}
