package cn.odboy.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.odboy.base.PageResult;
import cn.odboy.constant.CacheKey;
import cn.odboy.exception.BadRequestException;
import cn.odboy.exception.EntityExistException;
import cn.odboy.modules.security.service.UserCacheManager;
import cn.odboy.modules.security.service.dto.AuthorityDto;
import cn.odboy.modules.system.domain.Menu;
import cn.odboy.modules.system.domain.Role;
import cn.odboy.modules.system.domain.User;
import cn.odboy.modules.system.domain.dto.RoleQueryCriteria;
import cn.odboy.modules.system.mapper.RoleDeptMapper;
import cn.odboy.modules.system.mapper.RoleMapper;
import cn.odboy.modules.system.mapper.RoleMenuMapper;
import cn.odboy.modules.system.mapper.UserMapper;
import cn.odboy.modules.system.service.RoleService;
import cn.odboy.util.*;
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
    private final RedisUtil redisUtil;
    private final UserMapper userMapper;
    private final UserCacheManager userCacheManager;

    @Override
    public List<Role> queryAll() {
        return roleMapper.queryAll();
    }

    @Override
    public List<Role> queryAll(RoleQueryCriteria criteria) {
        return roleMapper.findAll(criteria);
    }

    @Override
    public PageResult<Role> queryAll(RoleQueryCriteria criteria, Page<Role> page) {
        List<Role> roles = roleMapper.findAll(criteria, page);
        Long total = roleMapper.countAll(criteria);
        return PageUtil.toPage(roles, total);
    }

    @Override
    public Role findById(long id) {
        String key = CacheKey.ROLE_ID + id;
        Role role = redisUtil.get(key, Role.class);
        if (role == null) {
            role = roleMapper.findById(id);
            redisUtil.set(key, role, 1, TimeUnit.DAYS);
        }
        return role;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Role resources) {
        if (roleMapper.findByName(resources.getName()) != null) {
            throw new EntityExistException(Role.class, "username", resources.getName());
        }
        save(resources);
        // 判断是否有部门数据，若有，则需创建关联
        if (CollectionUtil.isNotEmpty(resources.getDepts())) {
            roleDeptMapper.insertData(resources.getId(), resources.getDepts());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Role resources) {
        Role role = getById(resources.getId());
        Role role1 = roleMapper.findByName(resources.getName());
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
            roleDeptMapper.insertData(resources.getId(), resources.getDepts());
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
        roleDeptMapper.deleteByRoleIds(ids);
        roleMenuMapper.deleteByRoleIds(ids);
    }

    @Override
    public List<Role> findByUsersId(Long userId) {
        String key = CacheKey.ROLE_USER + userId;
        List<Role> roles = redisUtil.getList(key, Role.class);
        if (CollUtil.isEmpty(roles)) {
            roles = roleMapper.findByUserId(userId);
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
            List<Role> roles = roleMapper.findByUserId(user.getId());
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
        return roleMapper.findByMenuId(menuId);
    }

    /**
     * 清理缓存
     *
     * @param id /
     */
    public void delCaches(Long id, List<User> users) {
        users = CollectionUtil.isEmpty(users) ? userMapper.findByRoleId(id) : users;
        if (CollectionUtil.isNotEmpty(users)) {
            users.forEach(item -> userCacheManager.cleanUserCache(item.getUsername()));
            Set<Long> userIds = users.stream().map(User::getId).collect(Collectors.toSet());
            redisUtil.delByKeys(CacheKey.DATA_USER, userIds);
            redisUtil.delByKeys(CacheKey.MENU_USER, userIds);
            redisUtil.delByKeys(CacheKey.ROLE_AUTH, userIds);
            redisUtil.delByKeys(CacheKey.ROLE_USER, userIds);
        }
        redisUtil.del(CacheKey.ROLE_ID + id);
    }
}
