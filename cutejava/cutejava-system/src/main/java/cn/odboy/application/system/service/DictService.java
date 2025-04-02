package cn.odboy.application.system.service;

import cn.odboy.base.PageResult;
import cn.odboy.model.system.domain.Dict;
import cn.odboy.model.system.request.CreateDictRequest;
import cn.odboy.model.system.request.QueryDictRequest;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;


public interface DictService extends IService<Dict> {

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    PageResult<Dict> queryDictPage(QueryDictRequest criteria, Page<Object> page);

    /**
     * 查询全部数据
     *
     * @param criteria /
     * @return /
     */
    List<Dict> selectDictByCriteria(QueryDictRequest criteria);

    /**
     * 创建
     *
     * @param resources /
     */
    void saveDict(CreateDictRequest resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void updateDictById(Dict resources);

    /**
     * 删除
     *
     * @param ids /
     */
    void deleteDictByIds(Set<Long> ids);

    /**
     * 导出数据
     *
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void downloadExcel(List<Dict> queryAll, HttpServletResponse response) throws IOException;
}