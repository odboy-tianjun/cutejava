package cn.odboy.core.service.system;

import cn.hutool.core.collection.CollUtil;
import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.redis.system.SystemRedisKey;
import cn.odboy.core.dal.dataobject.system.SystemMenuTb;
import cn.odboy.core.dal.dataobject.system.SystemRoleTb;
import cn.odboy.core.dal.dataobject.system.SystemUserTb;
import cn.odboy.core.dal.mysql.system.SystemRoleMapper;
import cn.odboy.core.dal.mysql.system.SystemUserMapper;
import cn.odboy.core.dal.model.system.QuerySystemRoleArgs;
import cn.odboy.core.dal.model.system.SystemRoleCodeVo;
import cn.odboy.exception.BadRequestException;
import cn.odboy.redis.RedisHelper;
import cn.odboy.util.PageUtil;
import cn.odboy.util.StringUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SystemRoleApiImpl implements SystemRoleApi {
    private final SystemRoleMapper roleMapper;
    private final SystemUserMapper userMapper;
    private final RedisHelper redisHelper;

    @Override
    public List<SystemRoleTb> describeRoleList() {
        return roleMapper.queryRoleList();
    }

    @Override
    public List<SystemRoleTb> describeRoleList(QuerySystemRoleArgs criteria) {
        return roleMapper.queryRoleListByArgs(criteria);
    }

    @Override
    public CsResultVo<List<SystemRoleTb>> describeRolePage(QuerySystemRoleArgs criteria, Page<Object> page) {
        criteria.setOffset(page.offset());
        List<SystemRoleTb> roles = roleMapper.queryRoleListByArgs(criteria);
        Long total = roleMapper.getRoleCountByArgs(criteria);
        return PageUtil.toPage(roles, total);
    }

    @Override
    public SystemRoleTb describeRoleById(long id) {
        String key = SystemRedisKey.ROLE_ID + id;
        SystemRoleTb role = redisHelper.get(key, SystemRoleTb.class);
        if (role == null) {
            role = roleMapper.selectById(id);
            redisHelper.set(key, role, 1, TimeUnit.DAYS);
        }
        return role;
    }

    @Override
    public List<SystemRoleTb> describeRoleListByUsersId(Long userId) {
        String key = SystemRedisKey.ROLE_USER + userId;
        List<SystemRoleTb> roles = redisHelper.getList(key, SystemRoleTb.class);
        if (CollUtil.isEmpty(roles)) {
            roles = roleMapper.queryRoleListByUserId(userId);
            redisHelper.set(key, roles, 1, TimeUnit.DAYS);
        }
        return roles;
    }

    @Override
    public Integer describeDeptLevelByRoles(Set<SystemRoleTb> roles) {
        if (CollUtil.isEmpty(roles)) {
            return Integer.MAX_VALUE;
        }
        Set<SystemRoleTb> roleSet = new HashSet<>();
        for (SystemRoleTb role : roles) {
            roleSet.add(describeRoleById(role.getId()));
        }
        return Collections.min(roleSet.stream().map(SystemRoleTb::getLevel).collect(Collectors.toList()));
    }

    @Override
    public List<SystemRoleCodeVo> buildUserRolePermissions(SystemUserTb user) {
        String key = SystemRedisKey.ROLE_AUTH + user.getId();
        List<SystemRoleCodeVo> authorityList = redisHelper.getList(key, SystemRoleCodeVo.class);
        if (CollUtil.isEmpty(authorityList)) {
            Set<String> permissions = new HashSet<>();
            // 如果是管理员直接返回
            if (user.getIsAdmin()) {
                permissions.add("admin");
                return permissions.stream().map(SystemRoleCodeVo::new)
                        .collect(Collectors.toList());
            }
            List<SystemRoleTb> roles = roleMapper.queryRoleListByUserId(user.getId());
            permissions = roles.stream().flatMap(role -> role.getMenus().stream())
                    .map(SystemMenuTb::getPermission)
                    .filter(StringUtil::isNotBlank).collect(Collectors.toSet());
            authorityList = permissions.stream().map(SystemRoleCodeVo::new)
                    .collect(Collectors.toList());
            redisHelper.set(key, authorityList, 1, TimeUnit.HOURS);
        }
        return authorityList;
    }

    @Override
    public void verifyBindRelationByIds(Set<Long> ids) {
        if (userMapper.getUserCountByRoleIds(ids) > 0) {
            throw new BadRequestException("所选角色存在用户关联，请解除关联再试！");
        }
    }

    @Override
    public List<SystemRoleTb> describeRoleListByMenuId(Long menuId) {
        return roleMapper.queryRoleListByMenuId(menuId);
    }
}
