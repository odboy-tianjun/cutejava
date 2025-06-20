package cn.odboy.core.service.system;

import cn.odboy.core.service.system.SystemRoleApi;
import cn.odboy.core.dal.redis.system.SystemRedisKey;
import cn.odboy.core.constant.TransferProtocolConst;
import cn.odboy.core.dal.dataobject.system.SystemMenuTb;
import cn.odboy.core.dal.dataobject.system.SystemRoleTb;
import cn.odboy.core.dal.dataobject.system.SystemUserTb;
import cn.odboy.core.dal.mysql.system.SystemMenuMapper;
import cn.odboy.core.dal.mysql.system.SystemRoleMenuMapper;
import cn.odboy.core.dal.mysql.system.SystemUserMapper;
import cn.odboy.exception.BadRequestException;
import cn.odboy.exception.EntityExistException;
import cn.odboy.redis.RedisHelper;
import cn.odboy.util.FileUtil;
import cn.odboy.util.StringUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SystemMenuServiceImpl extends ServiceImpl<SystemMenuMapper, SystemMenuTb> implements SystemMenuService {
    private final SystemMenuMapper menuMapper;
    private final SystemRoleMenuMapper roleMenuMapper;
    private final SystemUserMapper userMapper;
    private final SystemRoleApi systemRoleApi;
    private final RedisHelper redisHelper;

    private static final String YES_STR = "是";
    private static final String NO_STR = "否";


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMenu(SystemMenuTb resources) {
        if (menuMapper.getMenuByTitle(resources.getTitle()) != null) {
            throw new EntityExistException(SystemMenuTb.class, "title", resources.getTitle());
        }
        if (StringUtil.isNotBlank(resources.getComponentName())) {
            if (menuMapper.getMenuByComponentName(resources.getComponentName()) != null) {
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
        SystemMenuTb menu1 = menuMapper.getMenuByTitle(resources.getTitle());

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
            menu1 = menuMapper.getMenuByComponentName(resources.getComponentName());
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
        // 清理缓存
        delCaches(resources.getId());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeMenuByIds(Set<SystemMenuTb> menuSet) {
        for (SystemMenuTb menu : menuSet) {
            // 清理缓存
            delCaches(menu.getId());
            roleMenuMapper.deleteByMenuId(menu.getId());
            menuMapper.deleteById(menu.getId());
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
            int count = menuMapper.getMenuCountByPid(menuId);
            menuMapper.updateSubCntByMenuId(count, menuId);
        }
    }

    /**
     * 清理缓存
     *
     * @param id 菜单ID
     */
    public void delCaches(Long id) {
        List<SystemUserTb> users = userMapper.queryUserListByMenuId(id);
        redisHelper.del(SystemRedisKey.MENU_ID + id);
        redisHelper.delByKeys(SystemRedisKey.MENU_USER, users.stream().map(SystemUserTb::getId).collect(Collectors.toSet()));
        // 清除 Role 缓存
        List<SystemRoleTb> roles = systemRoleApi.describeRoleListByMenuId(id);
        redisHelper.delByKeys(SystemRedisKey.ROLE_ID, roles.stream().map(SystemRoleTb::getId).collect(Collectors.toSet()));
    }

}
