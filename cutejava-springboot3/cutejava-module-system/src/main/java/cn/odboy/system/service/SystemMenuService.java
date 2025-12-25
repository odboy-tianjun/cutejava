/*
 * Copyright 2021-2025 Odboy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.odboy.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.framework.context.KitSpringBeanHolder;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.system.constant.TransferProtocolConst;
import cn.odboy.system.dal.dataobject.SystemMenuTb;
import cn.odboy.system.dal.dataobject.SystemRoleMenuTb;
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import cn.odboy.system.dal.model.SystemMenuMetaVo;
import cn.odboy.system.dal.model.SystemMenuVo;
import cn.odboy.system.dal.model.SystemQueryMenuArgs;
import cn.odboy.system.dal.model.SystemRoleVo;
import cn.odboy.system.dal.mysql.SystemMenuMapper;
import cn.odboy.system.dal.mysql.SystemRoleMenuMapper;
import cn.odboy.util.KitClassUtil;
import cn.odboy.util.KitFileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemMenuService {
    private static final String YES_STR = "是";
    private static final String NO_STR = "否";
    @Autowired private SystemMenuMapper systemMenuMapper;
    @Autowired private SystemRoleMenuMapper systemRoleMenuMapper;
    @Autowired private SystemRoleService systemRoleService;
    @Autowired private SystemRoleMenuService systemRoleMenuService;

    /**
     * 创建
     *
     * @param args /
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveMenu(SystemMenuTb args) {
        if (systemMenuMapper.getMenuByTitle(args.getTitle()) != null) {
            throw new BadRequestException("菜单标题已存在");
        }
        if (StrUtil.isNotBlank(args.getComponentName())) {
            if (systemMenuMapper.getMenuByComponentName(args.getComponentName()) != null) {
                throw new BadRequestException("菜单组件名称已存在");
            }
        }
        if (Long.valueOf(0L).equals(args.getPid())) {
            args.setPid(null);
        }
        if (args.getIFrame()) {
            if (!(args.getPath().toLowerCase().startsWith(TransferProtocolConst.PREFIX_HTTP) ||
                args.getPath().toLowerCase().startsWith(TransferProtocolConst.PREFIX_HTTPS))) {
                throw new BadRequestException(TransferProtocolConst.PREFIX_HTTPS_BAD_REQUEST);
            }
        }
        systemMenuMapper.insert(args);
        // 计算子节点数目
        args.setSubCount(0);
        // 更新父节点菜单数目
        this.updateMenuSubCnt(args.getPid());
    }

    /**
     * 编辑
     *
     * @param args /
     */
    @Transactional(rollbackFor = Exception.class)
    public void modifyMenuById(SystemMenuTb args) {
        if (args.getId().equals(args.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        SystemMenuTb menu = systemMenuMapper.selectById(args.getId());
        if (args.getIFrame()) {
            if (!(args.getPath().toLowerCase().startsWith(TransferProtocolConst.PREFIX_HTTP) ||
                args.getPath().toLowerCase().startsWith(TransferProtocolConst.PREFIX_HTTPS))) {
                throw new BadRequestException(TransferProtocolConst.PREFIX_HTTPS_BAD_REQUEST);
            }
        }
        SystemMenuTb menu1 = systemMenuMapper.getMenuByTitle(args.getTitle());
        if (menu1 != null && !menu1.getId().equals(menu.getId())) {
            throw new BadRequestException("菜单标题已存在");
        }
        if (args.getPid().equals(0L)) {
            args.setPid(null);
        }
        // 记录的父节点ID
        Long oldPid = menu.getPid();
        Long newPid = args.getPid();
        if (StrUtil.isNotBlank(args.getComponentName())) {
            menu1 = systemMenuMapper.getMenuByComponentName(args.getComponentName());
            if (menu1 != null && !menu1.getId().equals(menu.getId())) {
                throw new BadRequestException("菜单组件名称已存在");
            }
        }
        menu.setTitle(args.getTitle());
        menu.setComponent(args.getComponent());
        menu.setPath(args.getPath());
        menu.setIcon(args.getIcon());
        menu.setIFrame(args.getIFrame());
        menu.setPid(args.getPid());
        menu.setMenuSort(args.getMenuSort());
        menu.setCache(args.getCache());
        menu.setHidden(args.getHidden());
        menu.setComponentName(args.getComponentName());
        menu.setPermission(args.getPermission());
        menu.setType(args.getType());
        // auto fill
        menu.setUpdateBy(null);
        menu.setUpdateTime(null);
        systemMenuMapper.insertOrUpdate(menu);
        // 计算父级菜单节点数目
        this.updateMenuSubCnt(oldPid);
        this.updateMenuSubCnt(newPid);
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeMenuByIds(Set<Long> ids) {
        Set<SystemMenuTb> menuSet = new HashSet<>();
        for (Long id : ids) {
            List<SystemMenuTb> menuList = this.queryMenuByPid(id);
            menuSet.add(systemMenuMapper.selectById(id));
            menuSet = this.queryChildMenu(menuList, menuSet);
        }
        List<Long> menuIds =
            menuSet.stream().map(SystemMenuTb::getId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollUtil.isNotEmpty(menuIds)) {
            KitSpringBeanHolder.getBean(SystemMenuService.class).removeRoleMenuByMenuIds(menuIds);
            systemMenuMapper.deleteByIds(menuIds);
        }
        for (SystemMenuTb menu : menuSet) {
            this.updateMenuSubCnt(menu.getPid());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeRoleMenuByMenuIds(List<Long> menuIds) {
        if (CollUtil.isNotEmpty(menuIds)) {
            LambdaQueryWrapper<SystemRoleMenuTb> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(SystemRoleMenuTb::getMenuId, menuIds);
            systemRoleMenuMapper.delete(wrapper);
        }
    }

    /**
     * 导出
     *
     * @param menus    待导出的数据
     * @param response /
     * @throws IOException /
     */
    public void exportMenuExcel(List<SystemMenuTb> menus, HttpServletResponse response) throws IOException {
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
        KitFileUtil.downloadExcel(list, response);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateMenuSubCnt(Long menuId) {
        if (menuId != null) {
            long count = systemMenuMapper.countMenuByPid(menuId);
            systemMenuMapper.updateMenuSubCntByMenuId(count, menuId);
        }
    }

    /**
     * 查询全部数据
     *
     * @param criteria 条件
     * @param isQuery  /
     * @return /
     * @throws Exception /
     */
    public List<SystemMenuTb> queryAllMenu(SystemQueryMenuArgs criteria, Boolean isQuery) throws Exception {
        if (Boolean.TRUE.equals(isQuery)) {
            criteria.setPidIsNull(true);
            List<Field> fields = KitClassUtil.getAllFields(criteria.getClass(), new ArrayList<>());
            for (Field field : fields) {
                //设置对象的访问权限, 保证对private的属性的访问
                field.setAccessible(true);
                Object val = field.get(criteria);
                if ("pidIsNull".equals(field.getName())) {
                    continue;
                }
                // 如果有查询条件, 则不指定pidIsNull
                if (ObjectUtil.isNotNull(val)) {
                    criteria.setPidIsNull(null);
                    break;
                }
            }
        }
        return systemMenuMapper.selectMenuByArgs(criteria);
    }

    /**
     * 根据当前用户获取菜单
     *
     * @param currentUserId /
     * @return /
     */
    public List<SystemMenuTb> queryMenuListByUserId(Long currentUserId) {
        List<SystemRoleVo> roles = systemRoleService.queryRoleByUsersId(currentUserId);
        Set<Long> roleIds = roles.stream().map(SystemRoleTb::getId).collect(Collectors.toSet());
        return new ArrayList<>(systemRoleMenuService.queryMenuByRoleIdsAndType(roleIds, 2));
    }

    /**
     * 获取所有子节点, 包含自身ID
     *
     * @param menuList /
     * @param menuSet  /
     * @return /
     */
    public Set<SystemMenuTb> queryChildMenu(List<SystemMenuTb> menuList, Set<SystemMenuTb> menuSet) {
        for (SystemMenuTb menu : menuList) {
            menuSet.add(menu);
            List<SystemMenuTb> menus = systemMenuMapper.selectMenuByPid(menu.getId());
            if (CollUtil.isNotEmpty(menus)) {
                queryChildMenu(menus, menuSet);
            }
        }
        return menuSet;
    }

    /**
     * 懒加载菜单数据
     *
     * @param pid /
     * @return /
     */
    public List<SystemMenuTb> queryMenuByPid(Long pid) {
        List<SystemMenuTb> menus;
        if (pid != null && !pid.equals(0L)) {
            menus = systemMenuMapper.selectMenuByPid(pid);
        } else {
            menus = systemMenuMapper.selectMenuByPidIsNull();
        }
        return menus;
    }

    /**
     * 根据ID获取同级与上级数据
     *
     * @param menu  /
     * @param menus /
     * @return /
     */
    public List<SystemMenuTb> querySuperiorMenuList(SystemMenuTb menu, List<SystemMenuTb> menus) {
        if (menu.getPid() == null) {
            menus.addAll(systemMenuMapper.selectMenuByPidIsNull());
            return menus;
        }
        menus.addAll(systemMenuMapper.selectMenuByPid(menu.getPid()));
        return querySuperiorMenuList(systemMenuMapper.selectById(menu.getPid()), menus);
    }

    /**
     * 构建菜单树
     *
     * @param menus 原始数据
     * @return /
     */
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

    /**
     * 构建菜单树
     *
     * @param menus /
     * @return /
     */
    public List<SystemMenuVo> buildMenuVo(List<SystemMenuTb> menus) {
        List<SystemMenuVo> list = new LinkedList<>();
        menus.forEach(menu -> {
            if (menu != null) {
                List<SystemMenuTb> menuList = menu.getChildren();
                SystemMenuVo menuVo = new SystemMenuVo();
                menuVo.setName(
                    ObjectUtil.isNotEmpty(menu.getComponentName()) ? menu.getComponentName() : menu.getTitle());
                // 一级目录需要加斜杠, 不然会报警告
                menuVo.setPath(menu.getPid() == null ? "/" + menu.getPath() : menu.getPath());
                menuVo.setHidden(menu.getHidden());
                // 如果不是外链
                if (!menu.getIFrame()) {
                    if (menu.getPid() == null) {
                        menuVo.setComponent(StrUtil.isEmpty(menu.getComponent()) ? "Layout" : menu.getComponent());
                        // 如果不是一级菜单，并且菜单类型为目录，则代表是多级菜单
                    } else if (menu.getType() == 0) {
                        menuVo.setComponent(StrUtil.isEmpty(menu.getComponent()) ? "ParentView" : menu.getComponent());
                    } else if (StrUtil.isNotBlank(menu.getComponent())) {
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
        });
        return list;
    }

    /**
     * 获取 MenuResponse
     *
     * @param menu   /
     * @param menuVo /
     * @return /
     */
    private SystemMenuVo getMenuVo(SystemMenuTb menu, SystemMenuVo menuVo) {
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

    public List<SystemMenuTb> queryMenuByIds(List<Long> ids) {
        return systemMenuMapper.selectByIds(ids);
    }
}
