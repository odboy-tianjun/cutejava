package cn.odboy.application.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.odboy.application.core.service.UserCacheService;
import cn.odboy.application.system.mapper.RoleDeptMapper;
import cn.odboy.application.system.mapper.RoleMapper;
import cn.odboy.application.system.mapper.RoleMenuMapper;
import cn.odboy.application.system.mapper.UserMapper;
import cn.odboy.application.system.service.RoleService;
import cn.odboy.base.PageResult;
import cn.odboy.constant.SystemRedisKey;
import cn.odboy.exception.BadRequestException;
import cn.odboy.exception.EntityExistException;
import cn.odboy.model.system.domain.Menu;
import cn.odboy.model.system.domain.Role;
import cn.odboy.model.system.domain.User;
import cn.odboy.model.system.dto.AuthorityDto;
import cn.odboy.model.system.dto.RoleQueryCriteria;
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
    public List<Role> selectRole() {
        return roleMapper.selectRole();
    }

    @Override
    public List<Role> selectRoleByCriteria(RoleQueryCriteria criteria) {
        return roleMapper.selectRoleByCriteria(criteria);
    }

    @Override
    public PageResult<Role> queryRolePage(RoleQueryCriteria criteria, Page<Object> page) {
        criteria.setOffset(page.offset());
        List<Role> roles = roleMapper.selectRoleByCriteria(criteria);
        Long total = roleMapper.getRoleCountByCriteria(criteria);
        return PageUtil.toPage(roles, total);
    }

    @Override
    public Role getRoleById(long id) {
        String key = SystemRedisKey.ROLE_ID + id;
        Role role = redisUtil.get(key, Role.class);
        if (role == null) {
            role = roleMapper.selectById(id);
            redisUtil.set(key, role, 1, TimeUnit.DAYS);
        }
        return role;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRole(Role resources) {
        if (roleMapper.getRoleByName(resources.getName()) != null) {
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
    public void updateRoleById(Role resources) {
        Role role = getById(resources.getId());
        Role role1 = roleMapper.getRoleByName(resources.getName());
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
    public void updateMenuById(Role role) {
        List<User> users = userMapper.selectUserByRoleId(role.getId());
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
    public void deleteRoleByIds(Set<Long> ids) {
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
    public List<Role> selectRoleByUsersId(Long userId) {
        String key = SystemRedisKey.ROLE_USER + userId;
        List<Role> roles = redisUtil.getList(key, Role.class);
        if (CollUtil.isEmpty(roles)) {
            roles = roleMapper.selectRoleByUserId(userId);
            redisUtil.set(key, roles, 1, TimeUnit.DAYS);
        }
        return roles;
    }

    @Override
    public Integer getDeptLevelByRoles(Set<Role> roles) {
        if (CollUtil.isEmpty(roles)) {
            return Integer.MAX_VALUE;
        }
        Set<Role> roleSet = new HashSet<>();
        for (Role role : roles) {
            roleSet.add(getRoleById(role.getId()));
        }
        return Collections.min(roleSet.stream().map(Role::getLevel).collect(Collectors.toList()));
    }

    @Override
    public List<AuthorityDto> buildPermissions(User user) {
        String key = SystemRedisKey.ROLE_AUTH + user.getId();
        List<AuthorityDto> authorityList = redisUtil.getList(key, AuthorityDto.class);
        if (CollUtil.isEmpty(authorityList)) {
            Set<String> permissions = new HashSet<>();
            // 如果是管理员直接返回
            if (user.getIsAdmin()) {
                permissions.add("admin");
                return permissions.stream().map(AuthorityDto::new)
                        .collect(Collectors.toList());
            }
            List<Role> roles = roleMapper.selectRoleByUserId(user.getId());
            permissions = roles.stream().flatMap(role -> role.getMenus().stream())
                    .map(Menu::getPermission)
                    .filter(StringUtil::isNotBlank).collect(Collectors.toSet());
            authorityList = permissions.stream().map(AuthorityDto::new)
                    .collect(Collectors.toList());
            redisUtil.set(key, authorityList, 1, TimeUnit.HOURS);
        }
        return authorityList;
    }

    @Override
    public void downloadExcel(List<Role> roles, HttpServletResponse response) throws IOException {
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
    public void verifyBindRelationByIds(Set<Long> ids) {
        if (userMapper.getCountByRoles(ids) > 0) {
            throw new BadRequestException("所选角色存在用户关联，请解除关联再试！");
        }
    }

    @Override
    public List<Role> selectRoleByMenuId(Long menuId) {
        return roleMapper.selectRoleByMenuId(menuId);
    }

    /**
     * 清理缓存
     *
     * @param id /
     */
    public void delCaches(Long id, List<User> users) {
        users = CollectionUtil.isEmpty(users) ? userMapper.selectUserByRoleId(id) : users;
        if (CollectionUtil.isNotEmpty(users)) {
            users.forEach(item -> userCacheService.cleanUserCacheByUsername(item.getUsername()));
            Set<Long> userIds = users.stream().map(User::getId).collect(Collectors.toSet());
            redisUtil.delByKeys(SystemRedisKey.DATA_USER, userIds);
            redisUtil.delByKeys(SystemRedisKey.MENU_USER, userIds);
            redisUtil.delByKeys(SystemRedisKey.ROLE_AUTH, userIds);
            redisUtil.delByKeys(SystemRedisKey.ROLE_USER, userIds);
        }
        redisUtil.del(SystemRedisKey.ROLE_ID + id);
    }
}
