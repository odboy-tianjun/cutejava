package cn.odboy.application.system.service;

import cn.odboy.base.PageResult;
import cn.odboy.model.system.domain.Role;
import cn.odboy.model.system.domain.User;
import cn.odboy.model.system.dto.RoleCodeDto;
import cn.odboy.model.system.request.RoleQueryCriteria;
import cn.odboy.model.system.request.CreateRoleRequest;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;


public interface RoleService extends IService<Role> {

    /**
     * 查询全部数据
     *
     * @return /
     */
    List<Role> selectRole();

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @return /
     */
    List<Role> selectRoleByCriteria(RoleQueryCriteria criteria);

    /**
     * 待条件分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    PageResult<Role> queryRolePage(RoleQueryCriteria criteria, Page<Object> page);

    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */
    Role getRoleById(long id);

    /**
     * 创建
     *
     * @param resources /
     */
    void createRole(CreateRoleRequest resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void updateRoleById(Role resources);

    /**
     * 修改绑定的菜单
     *
     * @param resources /
     */
    void updateMenuById(Role resources);

    /**
     * 删除
     *
     * @param ids /
     */
    void deleteRoleByIds(Set<Long> ids);

    /**
     * 根据用户ID查询
     *
     * @param userId 用户ID
     * @return /
     */
    List<Role> selectRoleByUsersId(Long userId);

    /**
     * 根据角色查询角色级别
     *
     * @param roles /
     * @return /
     */
    Integer getDeptLevelByRoles(Set<Role> roles);

    /**
     * 获取用户权限信息
     *
     * @param user 用户信息
     * @return 权限信息
     */
    List<RoleCodeDto> buildPermissions(User user);

    /**
     * 导出数据
     *
     * @param roles    待导出的数据
     * @param response /
     * @throws IOException /
     */
    void downloadExcel(List<Role> roles, HttpServletResponse response) throws IOException;

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
    List<Role> selectRoleByMenuId(Long menuId);
}
