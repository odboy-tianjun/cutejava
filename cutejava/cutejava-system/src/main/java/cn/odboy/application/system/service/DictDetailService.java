package cn.odboy.application.system.service;

import cn.odboy.base.PageResult;
import cn.odboy.model.system.domain.DictDetail;
import cn.odboy.model.system.request.DictDetailQueryCriteria;
import cn.odboy.model.system.request.CreateDictDetailRequest;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface DictDetailService extends IService<DictDetail> {

    /**
     * 创建
     *
     * @param resources /
     */
    void saveDictDetail(CreateDictDetailRequest resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void updateDictDetailById(DictDetail resources);

    /**
     * 删除
     *
     * @param id /
     */
    void deleteDictDetailById(Long id);

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    PageResult<DictDetail> queryDictDetailPage(DictDetailQueryCriteria criteria, Page<Object> page);

    /**
     * 根据字典名称获取字典详情
     *
     * @param name 字典名称
     * @return /
     */
    List<DictDetail> selectDictDetailByName(String name);
}