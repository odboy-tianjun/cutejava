package cn.odboy.application.system.service;

import cn.odboy.model.system.domain.Menu;
import cn.odboy.model.system.request.QueryMenuRequest;
import cn.odboy.model.system.response.MenuResponse;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;


public interface MenuService extends IService<Menu> {
    /**
     * 查询全部数据
     *
     * @param criteria 条件
     * @param isQuery  /
     * @return /
     * @throws Exception /
     */
    List<Menu> selectMenuByCriteria(QueryMenuRequest criteria, Boolean isQuery) throws Exception;

    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */
    Menu getMenuById(long id);

    /**
     * 创建
     *
     * @param resources /
     */
    void saveMenu(Menu resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void updateMenuById(Menu resources);

    /**
     * 获取所有子节点，包含自身ID
     *
     * @param menuList /
     * @param menuSet  /
     * @return /
     */
    Set<Menu> selectChildMenus(List<Menu> menuList, Set<Menu> menuSet);

    /**
     * 构建菜单树
     *
     * @param menus 原始数据
     * @return /
     */
    List<Menu> buildTree(List<Menu> menus);

    /**
     * 构建菜单树
     *
     * @param menus /
     * @return /
     */
    List<MenuResponse> buildMenus(List<Menu> menus);

    /**
     * 删除
     *
     * @param menuSet /
     */
    void deleteMenuByIds(Set<Menu> menuSet);

    /**
     * 导出
     *
     * @param menus    待导出的数据
     * @param response /
     * @throws IOException /
     */
    void downloadExcel(List<Menu> menus, HttpServletResponse response) throws IOException;

    /**
     * 懒加载菜单数据
     *
     * @param pid /
     * @return /
     */
    List<Menu> selectMenuByPid(Long pid);

    /**
     * 根据ID获取同级与上级数据
     *
     * @param menu    /
     * @param objects /
     * @return /
     */
    List<Menu> selectSuperiorMenu(Menu menu, List<Menu> objects);

    /**
     * 根据当前用户获取菜单
     *
     * @param currentUserId /
     * @return /
     */
    List<Menu> selectMenuByUserId(Long currentUserId);
}
