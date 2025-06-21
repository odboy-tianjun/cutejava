package cn.odboy.core.service.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemJobTb;
import cn.odboy.core.dal.model.system.CreateSystemJobArgs;
import cn.odboy.core.dal.model.system.QuerySystemJobArgs;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;


public interface SystemJobService extends IService<SystemJobTb> {
    /**
     * 创建
     *
     * @param resources /
     */
    void saveJob(CreateSystemJobArgs resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void modifyJobById(SystemJobTb resources);

    /**
     * 删除
     *
     * @param ids /
     */
    void removeJobByIds(Set<Long> ids);

    /**
     * 导出数据
     *
     * @param jobs     待导出的数据
     * @param response /
     * @throws IOException /
     */
    void downloadJobExcel(List<SystemJobTb> jobs, HttpServletResponse response) throws IOException;
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
