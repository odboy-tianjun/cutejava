package cn.odboy.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.odboy.base.PageResult;
import cn.odboy.constant.CacheKey;
import cn.odboy.exception.BadRequestException;
import cn.odboy.exception.EntityExistException;
import cn.odboy.model.system.domain.Menu;
import cn.odboy.model.system.domain.Role;
import cn.odboy.model.system.domain.User;
import cn.odboy.model.system.dto.AuthorityDto;
import cn.odboy.model.system.dto.RoleQueryCriteria;
import cn.odboy.modules.security.service.UserCacheService;
import cn.odboy.modules.system.mapper.RoleDeptMapper;
import cn.odboy.modules.system.mapper.RoleMapper;
import cn.odboy.modules.system.mapper.RoleMenuMapper;
import cn.odboy.modules.system.mapper.UserMapper;
import cn.odboy.modules.system.service.RoleService;
import cn.odboy.util.FileUtil;
import cn.odboy.util.PageUtil;
import cn.odboy.util.RedisUtil;
import cn.odboy.util.StringUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    private final RoleMapper roleMapper;
    private final RoleDeptMapper roleDeptMapper;
    private final RoleMenuMapper roleMenuMapper;
    private final UserMapper userMapper;
    private final UserCacheService userCacheService;
    private final RedisUtil redisUtil;

    @Override
    public List<Role> queryAll() {
        return roleMapper.selectByPage();
    }

    @Override
    public List<Role> queryAll(RoleQueryCriteria criteria) {
        return roleMapper.selectByPage(criteria);
    }

    @Override
    public PageResult<Role> queryAll(RoleQueryCriteria criteria, Page<Object> page) {
        criteria.setOffset(page.offset());
        List<Role> roles = roleMapper.selectByPage(criteria);
        Long total = roleMapper.countByCriteria(criteria);
        return PageUtil.toPage(roles, total);
    }

    @Override
    public Role findById(long id) {
        String key = CacheKey.ROLE_ID + id;
        Role role = redisUtil.get(key, Role.class);
        if (role == null) {
            role = roleMapper.selectById(id);
            redisUtil.set(key, role, 1, TimeUnit.DAYS);
        }
        return role;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Role resources) {
        if (roleMapper.selectByName(resources.getName()) != null) {
            throw new EntityExistException(Role.class, "username", resources.getName());
        }
        save(resources);
        // 判断是否有部门数据，若有，则需创建关联
        if (CollectionUtil.isNotEmpty(resources.getDepts())) {
            roleDeptMapper.insertBatchByRoleId(resources.getId(), resources.getDepts());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Role resources) {
        Role role = getById(resources.getId());
        Role role1 = roleMapper.selectByName(resources.getName());
        if (role1 != null && !role1.getId().equals(role.getId())) {
            throw new EntityExistException(Role.class, "username", resources.getName());
        }
        role.setName(resources.getName());
        role.setDescription(resources.getDescription());
        role.setDataScope(resources.getDataScope());
        role.setDepts(resources.getDepts());
        role.setLevel(resources.getLevel());
        // 更新
        saveOrUpdate(role);
        // 删除关联部门数据
        roleDeptMapper.deleteByRoleId(resources.getId());
        // 判断是否有部门数据，若有，则需更新关联
        if (CollectionUtil.isNotEmpty(resources.getDepts())) {
            roleDeptMapper.insertBatchByRoleId(resources.getId(), resources.getDepts());
        }
        // 更新相关缓存
        delCaches(role.getId(), null);
    }

    @Override
    public void updateMenu(Role role) {
        List<User> users = userMapper.findByRoleId(role.getId());
        // 更新菜单
        roleMenuMapper.deleteByRoleId(role.getId());
        // 判断是否为空
        if (CollUtil.isNotEmpty(role.getMenus())) {
            roleMenuMapper.insertData(role.getId(), role.getMenus());
        }
        // 更新缓存
        delCaches(role.getId(), users);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            // 更新相关缓存
            delCaches(id, null);
        }
        removeBatchByIds(ids);
        // 删除角色部门关联数据、角色菜单关联数据
        roleDeptMapper.deleteBatchByRoleIds(ids);
        roleMenuMapper.deleteByRoleIds(ids);
    }

    @Override
    public List<Role> findByUsersId(Long userId) {
        String key = CacheKey.ROLE_USER + userId;
        List<Role> roles = redisUtil.getList(key, Role.class);
        if (CollUtil.isEmpty(roles)) {
            roles = roleMapper.selectByUserId(userId);
            redisUtil.set(key, roles, 1, TimeUnit.DAYS);
        }
        return roles;
    }

    @Override
    public Integer findByRoles(Set<Role> roles) {
        if (CollUtil.isEmpty(roles)) {
            return Integer.MAX_VALUE;
        }
        Set<Role> roleSet = new HashSet<>();
        for (Role role : roles) {
            roleSet.add(findById(role.getId()));
        }
        return Collections.min(roleSet.stream().map(Role::getLevel).collect(Collectors.toList()));
    }

    @Override
    public List<AuthorityDto> buildPermissions(User user) {
        String key = CacheKey.ROLE_AUTH + user.getId();
        List<AuthorityDto> authorityDtos = redisUtil.getList(key, AuthorityDto.class);
        if (CollUtil.isEmpty(authorityDtos)) {
            Set<String> permissions = new HashSet<>();
            // 如果是管理员直接返回
            if (user.getIsAdmin()) {
                permissions.add("admin");
                return permissions.stream().map(AuthorityDto::new)
                        .collect(Collectors.toList());
            }
            List<Role> roles = roleMapper.selectByUserId(user.getId());
            permissions = roles.stream().flatMap(role -> role.getMenus().stream())
                    .map(Menu::getPermission)
                    .filter(StringUtil::isNotBlank).collect(Collectors.toSet());
            authorityDtos = permissions.stream().map(AuthorityDto::new)
                    .collect(Collectors.toList());
            redisUtil.set(key, authorityDtos, 1, TimeUnit.HOURS);
        }
        return authorityDtos;
    }

    @Override
    public void download(List<Role> roles, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Role role : roles) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("角色名称", role.getName());
            map.put("角色级别", role.getLevel());
            map.put("描述", role.getDescription());
            map.put("创建日期", role.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void verification(Set<Long> ids) {
        if (userMapper.countByRoles(ids) > 0) {
            throw new BadRequestException("所选角色存在用户关联，请解除关联再试！");
        }
    }

    @Override
    public List<Role> findByMenuId(Long menuId) {
        return roleMapper.selectByMenuId(menuId);
    }

    /**
     * 清理缓存
     *
     * @param id /
     */
    public void delCaches(Long id, List<User> users) {
        users = CollectionUtil.isEmpty(users) ? userMapper.findByRoleId(id) : users;
        if (CollectionUtil.isNotEmpty(users)) {
            users.forEach(item -> userCacheService.cleanUserCache(item.getUsername()));
            Set<Long> userIds = users.stream().map(User::getId).collect(Collectors.toSet());
            redisUtil.delByKeys(CacheKey.DATA_USER, userIds);
            redisUtil.delByKeys(CacheKey.MENU_USER, userIds);
            redisUtil.delByKeys(CacheKey.ROLE_AUTH, userIds);
            redisUtil.delByKeys(CacheKey.ROLE_USER, userIds);
        }
        redisUtil.del(CacheKey.ROLE_ID + id);
    }
}
