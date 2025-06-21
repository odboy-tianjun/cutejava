package cn.odboy.core.service.system;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.odboy.core.constant.TransferProtocolConst;
import cn.odboy.core.dal.dataobject.system.SystemMenuTb;
import cn.odboy.core.dal.dataobject.system.SystemRoleTb;
import cn.odboy.core.dal.model.system.QuerySystemMenuArgs;
import cn.odboy.core.dal.model.system.SystemMenuMetaVo;
import cn.odboy.core.dal.model.system.SystemMenuVo;
import cn.odboy.core.dal.mysql.system.SystemMenuMapper;
import cn.odboy.core.dal.mysql.system.SystemRoleMenuMapper;
import cn.odboy.core.dal.mysql.system.SystemUserMapper;
import cn.odboy.exception.BadRequestException;
import cn.odboy.exception.EntityExistException;
import cn.odboy.util.ClassUtil;
import cn.odboy.util.FileUtil;
import cn.odboy.util.StringUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SystemMenuServiceImpl extends ServiceImpl<SystemMenuMapper, SystemMenuTb> implements SystemMenuService {
    private final SystemMenuMapper systemMenuMapper;
    private final SystemRoleMenuMapper systemRoleMenuMapper;
    private final SystemUserMapper systemUserMapper;
    private final SystemRoleService systemRoleService;

    private static final String YES_STR = "是";
    private static final String NO_STR = "否";


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMenu(SystemMenuTb resources) {
        if (systemMenuMapper.getMenuByTitle(resources.getTitle()) != null) {
            throw new EntityExistException(SystemMenuTb.class, "title", resources.getTitle());
        }
        if (StringUtil.isNotBlank(resources.getComponentName())) {
            if (systemMenuMapper.getMenuByComponentName(resources.getComponentName()) != null) {
                throw new EntityExistException(SystemMenuTb.class, "componentName", resources.getComponentName());
            }
        }
        if (Long.valueOf(0L).equals(resources.getPid())) {
            resources.setPid(null);
        }
        if (resources.getIFrame()) {
            if (!(resources.getPath().toLowerCase().startsWith(TransferProtocolConst.PREFIX_HTTP) || resources.getPath().toLowerCase().startsWith(TransferProtocolConst.PREFIX_HTTPS))) {
                throw new BadRequestException(TransferProtocolConst.PREFIX_HTTPS_BAD_REQUEST);
            }
        }
        save(resources);
        // 计算子节点数目
        resources.setSubCount(0);
        // 更新父节点菜单数目
        updateSubCnt(resources.getPid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyMenuById(SystemMenuTb resources) {
        if (resources.getId().equals(resources.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        SystemMenuTb menu = getById(resources.getId());
        if (resources.getIFrame()) {
            if (!(resources.getPath().toLowerCase().startsWith(TransferProtocolConst.PREFIX_HTTP) || resources.getPath().toLowerCase().startsWith(TransferProtocolConst.PREFIX_HTTPS))) {
                throw new BadRequestException(TransferProtocolConst.PREFIX_HTTPS_BAD_REQUEST);
            }
        }
        SystemMenuTb menu1 = systemMenuMapper.getMenuByTitle(resources.getTitle());

        if (menu1 != null && !menu1.getId().equals(menu.getId())) {
            throw new EntityExistException(SystemMenuTb.class, "title", resources.getTitle());
        }

        if (resources.getPid().equals(0L)) {
            resources.setPid(null);
        }

        // 记录的父节点ID
        Long oldPid = menu.getPid();
        Long newPid = resources.getPid();

        if (StringUtil.isNotBlank(resources.getComponentName())) {
            menu1 = systemMenuMapper.getMenuByComponentName(resources.getComponentName());
            if (menu1 != null && !menu1.getId().equals(menu.getId())) {
                throw new EntityExistException(SystemMenuTb.class, "componentName", resources.getComponentName());
            }
        }
        menu.setTitle(resources.getTitle());
        menu.setComponent(resources.getComponent());
        menu.setPath(resources.getPath());
        menu.setIcon(resources.getIcon());
        menu.setIFrame(resources.getIFrame());
        menu.setPid(resources.getPid());
        menu.setMenuSort(resources.getMenuSort());
        menu.setCache(resources.getCache());
        menu.setHidden(resources.getHidden());
        menu.setComponentName(resources.getComponentName());
        menu.setPermission(resources.getPermission());
        menu.setType(resources.getType());
        saveOrUpdate(menu);
        // 计算父级菜单节点数目
        updateSubCnt(oldPid);
        updateSubCnt(newPid);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeMenuByIds(Set<SystemMenuTb> menuSet) {
        for (SystemMenuTb menu : menuSet) {
            systemRoleMenuMapper.deleteByMenuId(menu.getId());
            systemMenuMapper.deleteById(menu.getId());
            updateSubCnt(menu.getPid());
        }
    }


    @Override
    public void downloadMenuExcel(List<SystemMenuTb> menus, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SystemMenuTb menu : menus) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("菜单标题", menu.getTitle());
            map.put("菜单类型", menu.getType() == null ? "目录" : menu.getType() == 1 ? "菜单" : "按钮");
            map.put("权限标识", menu.getPermission());
            map.put("外链菜单", menu.getIFrame() ? YES_STR : NO_STR);
            map.put("菜单可见", menu.getHidden() ? NO_STR : YES_STR);
            map.put("是否缓存", menu.getCache() ? YES_STR : NO_STR);
            map.put("创建日期", menu.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    private void updateSubCnt(Long menuId) {
        if (menuId != null) {
            int count = systemMenuMapper.getMenuCountByPid(menuId);
            systemMenuMapper.updateSubCntByMenuId(count, menuId);
        }
    }

    @Override
    public List<SystemMenuTb> describeMenuList(QuerySystemMenuArgs criteria, Boolean isQuery) throws Exception {
        if (Boolean.TRUE.equals(isQuery)) {
            criteria.setPidIsNull(true);
            List<Field> fields = ClassUtil.getAllFields(criteria.getClass(), new ArrayList<>());
            for (Field field : fields) {
                //设置对象的访问权限，保证对private的属性的访问
                field.setAccessible(true);
                Object val = field.get(criteria);
                if ("pidIsNull".equals(field.getName())) {
                    continue;
                }
                // 如果有查询条件，则不指定pidIsNull
                if (ObjectUtil.isNotNull(val)) {
                    criteria.setPidIsNull(null);
                    break;
                }
            }
        }
        return systemMenuMapper.queryMenuListByArgs(criteria);
    }

    @Override
    public SystemMenuTb describeMenuById(long id) {
        return systemMenuMapper.selectById(id);
    }

    /**
     * 用户角色改变时需清理缓存
     *
     * @param currentUserId /
     * @return /
     */
    @Override
    public List<SystemMenuTb> describeMenuListByUserId(Long currentUserId) {
        List<SystemRoleTb> roles = systemRoleService.describeRoleListByUsersId(currentUserId);
        Set<Long> roleIds = roles.stream().map(SystemRoleTb::getId).collect(Collectors.toSet());
        return new ArrayList<>(systemMenuMapper.queryMenuSetByRoleIdsAndType(roleIds, 2));
    }

    @Override
    public Set<SystemMenuTb> describeChildMenuSet(List<SystemMenuTb> menuList, Set<SystemMenuTb> menuSet) {
        for (SystemMenuTb menu : menuList) {
            menuSet.add(menu);
            List<SystemMenuTb> menus = systemMenuMapper.queryMenuListByPidOrderByMenuSort(menu.getId());
            if (CollUtil.isNotEmpty(menus)) {
                describeChildMenuSet(menus, menuSet);
            }
        }
        return menuSet;
    }

    @Override
    public List<SystemMenuTb> describeMenuListByPid(Long pid) {
        List<SystemMenuTb> menus;
        if (pid != null && !pid.equals(0L)) {
            menus = systemMenuMapper.queryMenuListByPidOrderByMenuSort(pid);
        } else {
            menus = systemMenuMapper.queryMenuListByPidIsNullOrderByMenuSort();
        }
        return menus;
    }

    @Override
    public List<SystemMenuTb> describeSuperiorMenuList(SystemMenuTb menu, List<SystemMenuTb> menus) {
        if (menu.getPid() == null) {
            menus.addAll(systemMenuMapper.queryMenuListByPidIsNullOrderByMenuSort());
            return menus;
        }
        menus.addAll(systemMenuMapper.queryMenuListByPidOrderByMenuSort(menu.getPid()));
        return describeSuperiorMenuList(describeMenuById(menu.getPid()), menus);
    }

    @Override
    public List<SystemMenuTb> buildMenuTree(List<SystemMenuTb> menus) {
        List<SystemMenuTb> trees = new ArrayList<>();
        Set<Long> ids = new HashSet<>();
        for (SystemMenuTb menu : menus) {
            if (menu.getPid() == null) {
                trees.add(menu);
            }
            for (SystemMenuTb it : menus) {
                if (menu.getId().equals(it.getPid())) {
                    if (menu.getChildren() == null) {
                        menu.setChildren(new ArrayList<>());
                    }
                    menu.getChildren().add(it);
                    ids.add(it.getId());
                }
            }
        }
        if (CollUtil.isNotEmpty(trees)) {
            trees = menus.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
        }
        return trees;
    }

    @Override
    public List<SystemMenuVo> buildMenuVo(List<SystemMenuTb> menus) {
        List<SystemMenuVo> list = new LinkedList<>();
        menus.forEach(menu -> {
                    if (menu != null) {
                        List<SystemMenuTb> menuList = menu.getChildren();
                        SystemMenuVo menuVo = new SystemMenuVo();
                        menuVo.setName(ObjectUtil.isNotEmpty(menu.getComponentName()) ? menu.getComponentName() : menu.getTitle());
                        // 一级目录需要加斜杠，不然会报警告
                        menuVo.setPath(menu.getPid() == null ? "/" + menu.getPath() : menu.getPath());
                        menuVo.setHidden(menu.getHidden());
                        // 如果不是外链
                        if (!menu.getIFrame()) {
                            if (menu.getPid() == null) {
                                menuVo.setComponent(StringUtil.isEmpty(menu.getComponent()) ? "Layout" : menu.getComponent());
                                // 如果不是一级菜单，并且菜单类型为目录，则代表是多级菜单
                            } else if (menu.getType() == 0) {
                                menuVo.setComponent(StringUtil.isEmpty(menu.getComponent()) ? "ParentView" : menu.getComponent());
                            } else if (StringUtil.isNoneBlank(menu.getComponent())) {
                                menuVo.setComponent(menu.getComponent());
                            }
                        }
                        menuVo.setMeta(new SystemMenuMetaVo(menu.getTitle(), menu.getIcon(), !menu.getCache()));
                        if (CollectionUtil.isNotEmpty(menuList)) {
                            menuVo.setAlwaysShow(true);
                            menuVo.setRedirect("noredirect");
                            menuVo.setChildren(buildMenuVo(menuList));
                            // 处理是一级菜单并且没有子菜单的情况
                        } else if (menu.getPid() == null) {
                            SystemMenuVo menuVo1 = getMenuVo(menu, menuVo);
                            menuVo.setName(null);
                            menuVo.setMeta(null);
                            menuVo.setComponent("Layout");
                            List<SystemMenuVo> list1 = new ArrayList<>();
                            list1.add(menuVo1);
                            menuVo.setChildren(list1);
                        }
                        list.add(menuVo);
                    }
                }
        );
        return list;
    }

    /**
     * 获取 MenuResponse
     *
     * @param menu   /
     * @param menuVo /
     * @return /
     */
    private static SystemMenuVo getMenuVo(SystemMenuTb menu, SystemMenuVo menuVo) {
        SystemMenuVo menuVo1 = new SystemMenuVo();
        menuVo1.setMeta(menuVo.getMeta());
        // 非外链
        if (!menu.getIFrame()) {
            menuVo1.setPath("index");
            menuVo1.setName(menuVo.getName());
            menuVo1.setComponent(menuVo.getComponent());
        } else {
            menuVo1.setPath(menu.getPath());
        }
        return menuVo1;
    }
}
