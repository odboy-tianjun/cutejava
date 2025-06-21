package cn.odboy.core.service.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemDictTb;
import cn.odboy.core.dal.model.system.CreateSystemDictArgs;
import cn.odboy.core.dal.model.system.QuerySystemDictArgs;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;


public interface SystemDictService extends IService<SystemDictTb> {
    /**
     * 创建
     *
     * @param resources /
     */
    void saveDict(CreateSystemDictArgs resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void modifyDictById(SystemDictTb resources);

    /**
     * 删除
     *
     * @param ids /
     */
    void removeDictByIds(Set<Long> ids);

    /**
     * 导出数据
     *
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void downloadDictExcel(List<SystemDictTb> queryAll, HttpServletResponse response) throws IOException;

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    CsResultVo<List<SystemDictTb>> queryDictPage(QuerySystemDictArgs criteria, Page<SystemDictTb> page);

    /**
     * 查询全部数据
     *
     * @param criteria /
     * @return /
     */
    List<SystemDictTb> queryDictList(QuerySystemDictArgs criteria);
}
