package cn.odboy.core.service.system;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.odboy.core.dal.redis.system.SystemRedisKey;
import cn.odboy.core.dal.dataobject.system.SystemMenuTb;
import cn.odboy.core.dal.dataobject.system.SystemRoleTb;
import cn.odboy.core.dal.model.system.SystemMenuVo;
import cn.odboy.core.dal.mysql.system.SystemMenuMapper;
import cn.odboy.core.dal.model.system.SystemMenuMetaVo;
import cn.odboy.core.dal.model.system.QuerySystemMenuArgs;
import cn.odboy.redis.RedisHelper;
import cn.odboy.util.ClassUtil;
import cn.odboy.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SystemMenuApiImpl implements SystemMenuApi {
    private final SystemMenuMapper menuMapper;
    private final SystemRoleApi systemRoleApi;
    private final SystemRoleService systemRoleService;
    private final RedisHelper redisHelper;

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
        return menuMapper.queryMenuListByArgs(criteria);
    }

    @Override
    public SystemMenuTb describeMenuById(long id) {
        String key = SystemRedisKey.MENU_ID + id;
        SystemMenuTb menu = redisHelper.get(key, SystemMenuTb.class);
        if (menu == null) {
            menu = menuMapper.selectById(id);
            redisHelper.set(key, menu, 1, TimeUnit.DAYS);
        }
        return menu;
    }

    /**
     * 用户角色改变时需清理缓存
     *
     * @param currentUserId /
     * @return /
     */
    @Override
    public List<SystemMenuTb> describeMenuListByUserId(Long currentUserId) {
        String key = SystemRedisKey.MENU_USER + currentUserId;
        List<SystemMenuTb> menus = redisHelper.getList(key, SystemMenuTb.class);
        if (CollUtil.isEmpty(menus)) {
            List<SystemRoleTb> roles = systemRoleApi.describeRoleListByUsersId(currentUserId);
            Set<Long> roleIds = roles.stream().map(SystemRoleTb::getId).collect(Collectors.toSet());
            menus = new ArrayList<>(menuMapper.queryMenuSetByRoleIdsAndType(roleIds, 2));
            redisHelper.set(key, menus, 1, TimeUnit.DAYS);
        }
        return menus;
    }

    @Override
    public Set<SystemMenuTb> describeChildMenuSet(List<SystemMenuTb> menuList, Set<SystemMenuTb> menuSet) {
        for (SystemMenuTb menu : menuList) {
            menuSet.add(menu);
            List<SystemMenuTb> menus = menuMapper.queryMenuListByPidOrderByMenuSort(menu.getId());
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
            menus = menuMapper.queryMenuListByPidOrderByMenuSort(pid);
        } else {
            menus = menuMapper.queryMenuListByPidIsNullOrderByMenuSort();
        }
        return menus;
    }

    @Override
    public List<SystemMenuTb> describeSuperiorMenuList(SystemMenuTb menu, List<SystemMenuTb> menus) {
        if (menu.getPid() == null) {
            menus.addAll(menuMapper.queryMenuListByPidIsNullOrderByMenuSort());
            return menus;
        }
        menus.addAll(menuMapper.queryMenuListByPidOrderByMenuSort(menu.getPid()));
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
