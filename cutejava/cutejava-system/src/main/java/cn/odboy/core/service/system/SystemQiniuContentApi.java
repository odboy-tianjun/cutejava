package cn.odboy.core.service.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemQiniuContentTb;
import cn.odboy.core.dal.model.system.QuerySystemQiniuArgs;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface SystemQiniuContentApi {

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    CsResultVo<List<SystemQiniuContentTb>> describeQiniuContentPage(QuerySystemQiniuArgs criteria, Page<SystemQiniuContentTb> page);

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @return /
     */
    List<SystemQiniuContentTb> describeQiniuContentList(QuerySystemQiniuArgs criteria);
}
