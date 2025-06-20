package cn.odboy.core.service.system;

import cn.odboy.core.dal.dataobject.system.SystemMenuTb;
import cn.odboy.core.dal.model.system.QuerySystemMenuArgs;
import cn.odboy.core.dal.model.system.SystemMenuVo;

import java.util.List;
import java.util.Set;

public interface SystemMenuApi {
    /**
     * 查询全部数据
     *
     * @param criteria 条件
     * @param isQuery  /
     * @return /
     * @throws Exception /
     */
    List<SystemMenuTb> describeMenuList(QuerySystemMenuArgs criteria, Boolean isQuery) throws Exception;

    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */
    SystemMenuTb describeMenuById(long id);

    /**
     * 获取所有子节点，包含自身ID
     *
     * @param menuList /
     * @param menuSet  /
     * @return /
     */
    Set<SystemMenuTb> describeChildMenuSet(List<SystemMenuTb> menuList, Set<SystemMenuTb> menuSet);

    /**
     * 构建菜单树
     *
     * @param menus 原始数据
     * @return /
     */
    List<SystemMenuTb> buildMenuTree(List<SystemMenuTb> menus);

    /**
     * 构建菜单树
     *
     * @param menus /
     * @return /
     */
    List<SystemMenuVo> buildMenuVo(List<SystemMenuTb> menus);

    /**
     * 懒加载菜单数据
     *
     * @param pid /
     * @return /
     */
    List<SystemMenuTb> describeMenuListByPid(Long pid);

    /**
     * 根据ID获取同级与上级数据
     *
     * @param menu    /
     * @param objects /
     * @return /
     */
    List<SystemMenuTb> describeSuperiorMenuList(SystemMenuTb menu, List<SystemMenuTb> objects);

    /**
     * 根据当前用户获取菜单
     *
     * @param currentUserId /
     * @return /
     */
    List<SystemMenuTb> describeMenuListByUserId(Long currentUserId);
}
