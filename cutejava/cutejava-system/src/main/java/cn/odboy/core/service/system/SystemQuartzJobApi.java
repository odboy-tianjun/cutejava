package cn.odboy.core.service.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemQuartzJobTb;
import cn.odboy.core.dal.dataobject.system.SystemQuartzLogTb;
import cn.odboy.core.dal.model.system.QuerySystemQuartzJobArgs;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface SystemQuartzJobApi {
    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    CsResultVo<List<SystemQuartzJobTb>> describeQuartzJobPage(QuerySystemQuartzJobArgs criteria, Page<SystemQuartzJobTb> page);

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @return /
     */
    List<SystemQuartzJobTb> describeQuartzJobList(QuerySystemQuartzJobArgs criteria);

    /**
     * 分页查询日志
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    CsResultVo<List<SystemQuartzLogTb>> describeQuartzLogPage(QuerySystemQuartzJobArgs criteria, Page<SystemQuartzLogTb> page);

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @return /
     */
    List<SystemQuartzLogTb> describeQuartzLogList(QuerySystemQuartzJobArgs criteria);
}
