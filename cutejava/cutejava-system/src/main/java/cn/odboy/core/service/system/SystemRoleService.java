package cn.odboy.core.service.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemRoleTb;
import cn.odboy.core.dal.dataobject.system.SystemUserTb;
import cn.odboy.core.dal.model.system.CreateSystemRoleArgs;
import cn.odboy.core.dal.model.system.QuerySystemRoleArgs;
import cn.odboy.core.dal.model.system.SystemRoleCodeVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;


public interface SystemRoleService extends IService<SystemRoleTb> {


    /**
     * 创建
     *
     * @param resources /
     */
    void saveRole(CreateSystemRoleArgs resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void modifyRoleById(SystemRoleTb resources);

    /**
     * 修改绑定的菜单
     *
     * @param resources /
     */
    void modifyBindMenuById(SystemRoleTb resources);

    /**
     * 删除
     *
     * @param ids /
     */
    void removeRoleByIds(Set<Long> ids);


    /**
     * 导出数据
     *
     * @param roles    待导出的数据
     * @param response /
     * @throws IOException /
     */
    void downloadRoleExcel(List<SystemRoleTb> roles, HttpServletResponse response) throws IOException;

    /**
     * 查询全部数据
     *
     * @return /
     */
    List<SystemRoleTb> queryRoleList();

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @return /
     */
    List<SystemRoleTb> queryRoleList(QuerySystemRoleArgs criteria);

    /**
     * 待条件分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    CsResultVo<List<SystemRoleTb>> queryRolePage(QuerySystemRoleArgs criteria, Page<Object> page);

    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */
    SystemRoleTb queryRoleById(long id);


    /**
     * 根据用户ID查询
     *
     * @param userId 用户ID
     * @return /
     */
    List<SystemRoleTb> queryRoleListByUsersId(Long userId);

    /**
     * 根据角色查询角色级别
     *
     * @param roles /
     * @return /
     */
    Integer queryDeptLevelByRoles(Set<SystemRoleTb> roles);

    /**
     * 获取用户权限信息
     *
     * @param user 用户信息
     * @return 权限信息
     */
    List<SystemRoleCodeVo> buildUserRolePermissions(SystemUserTb user);

    /**
     * 验证是否被用户关联
     *
     * @param ids /
     */
    void verifyBindRelationByIds(Set<Long> ids);

    /**
     * 根据菜单Id查询
     *
     * @param menuId /
     * @return /
     */
    List<SystemRoleTb> queryRoleListByMenuId(Long menuId);
}
