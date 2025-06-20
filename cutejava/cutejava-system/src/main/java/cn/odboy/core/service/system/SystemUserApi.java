package cn.odboy.core.service.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemUserTb;
import cn.odboy.core.dal.model.system.QuerySystemUserArgs;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface SystemUserApi {

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return /
     */
    SystemUserTb describeUserById(long id);

    /**
     * 根据用户名查询
     *
     * @param username /
     * @return /
     */
    SystemUserTb describeUserByUsername(String username);

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    CsResultVo<List<SystemUserTb>> describeUserPage(QuerySystemUserArgs criteria, Page<Object> page);

    /**
     * 查询全部不分页
     *
     * @param criteria 条件
     * @return /
     */
    List<SystemUserTb> describeUserList(QuerySystemUserArgs criteria);

}
