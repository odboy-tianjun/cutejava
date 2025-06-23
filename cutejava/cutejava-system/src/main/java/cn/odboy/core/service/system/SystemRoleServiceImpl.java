package cn.odboy.core.service.system;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemMenuTb;
import cn.odboy.core.dal.dataobject.system.SystemRoleTb;
import cn.odboy.core.dal.dataobject.system.SystemUserTb;
import cn.odboy.core.dal.model.system.CreateSystemRoleArgs;
import cn.odboy.core.dal.model.system.QuerySystemRoleArgs;
import cn.odboy.core.dal.model.system.SystemRoleCodeVo;
import cn.odboy.core.dal.mysql.system.SystemRoleDeptMapper;
import cn.odboy.core.dal.mysql.system.SystemRoleMapper;
import cn.odboy.core.dal.mysql.system.SystemRoleMenuMapper;
import cn.odboy.core.dal.mysql.system.SystemUserMapper;
import cn.odboy.exception.BadRequestException;
import cn.odboy.util.FileUtil;
import cn.odboy.util.PageUtil;
import cn.odboy.util.StringUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public class SystemRoleServiceImpl extends ServiceImpl<SystemRoleMapper, SystemRoleTb> implements SystemRoleService {
    private final SystemRoleMapper systemRoleMapper;
    private final SystemRoleDeptMapper systemRoleDeptMapper;
    private final SystemRoleMenuMapper systemRoleMenuMapper;
    private final SystemUserMapper systemUserMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(CreateSystemRoleArgs resources) {
        if (systemRoleMapper.getRoleByName(resources.getName()) != null) {
            throw new BadRequestException("角色名称已存在");
        }
        save(BeanUtil.copyProperties(resources, SystemRoleTb.class));
        // 判断是否有部门数据，若有，则需创建关联
        if (CollectionUtil.isNotEmpty(resources.getDepts())) {
            systemRoleDeptMapper.insertBatchWithRoleId(resources.getDepts(), resources.getId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyRoleById(SystemRoleTb resources) {
        SystemRoleTb role = getById(resources.getId());
        SystemRoleTb role1 = systemRoleMapper.getRoleByName(resources.getName());
        if (role1 != null && !role1.getId().equals(role.getId())) {
            throw new BadRequestException("角色名称已存在");
        }
        role.setName(resources.getName());
        role.setDescription(resources.getDescription());
        role.setDataScope(resources.getDataScope());
        role.setDepts(resources.getDepts());
        role.setLevel(resources.getLevel());
        // 更新
        saveOrUpdate(role);
        // 删除关联部门数据
        systemRoleDeptMapper.deleteByRoleId(resources.getId());
        // 判断是否有部门数据，若有，则需更新关联
        if (CollectionUtil.isNotEmpty(resources.getDepts())) {
            systemRoleDeptMapper.insertBatchWithRoleId(resources.getDepts(), resources.getId());
        }
    }

    @Override
    public void modifyBindMenuById(SystemRoleTb role) {
        List<SystemUserTb> users = systemUserMapper.queryUserListByRoleId(role.getId());
        // 更新菜单
        systemRoleMenuMapper.deleteByRoleId(role.getId());
        // 判断是否为空
        if (CollUtil.isNotEmpty(role.getMenus())) {
            systemRoleMenuMapper.insertBatchWithRoleId(role.getMenus(), role.getId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRoleByIds(Set<Long> ids) {
        removeBatchByIds(ids);
        // 删除角色部门关联数据、角色菜单关联数据
        systemRoleDeptMapper.deleteByRoleIds(ids);
        systemRoleMenuMapper.deleteByRoleIds(ids);
    }

    @Override
    public void downloadRoleExcel(List<SystemRoleTb> roles, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SystemRoleTb role : roles) {
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
    public List<SystemRoleTb> queryRoleList() {
        return systemRoleMapper.queryRoleList();
    }

    @Override
    public List<SystemRoleTb> queryRoleList(QuerySystemRoleArgs criteria) {
        return systemRoleMapper.queryRoleListByArgs(criteria);
    }

    @Override
    public CsResultVo<List<SystemRoleTb>> queryRolePage(QuerySystemRoleArgs criteria, Page<Object> page) {
        criteria.setOffset(page.offset());
        List<SystemRoleTb> roles = systemRoleMapper.queryRoleListByArgs(criteria);
        Long total = systemRoleMapper.getRoleCountByArgs(criteria);
        return PageUtil.toPage(roles, total);
    }

    @Override
    public SystemRoleTb queryRoleById(long id) {
        return systemRoleMapper.selectById(id);
    }

    @Override
    public List<SystemRoleTb> queryRoleListByUsersId(Long userId) {
        return systemRoleMapper.queryRoleListByUserId(userId);
    }

    @Override
    public Integer queryDeptLevelByRoles(Set<SystemRoleTb> roles) {
        if (CollUtil.isEmpty(roles)) {
            return Integer.MAX_VALUE;
        }
        Set<SystemRoleTb> roleSet = new HashSet<>();
        for (SystemRoleTb role : roles) {
            roleSet.add(queryRoleById(role.getId()));
        }
        return Collections.min(roleSet.stream().map(SystemRoleTb::getLevel).collect(Collectors.toList()));
    }

    @Override
    public List<SystemRoleCodeVo> buildUserRolePermissions(SystemUserTb user) {
        Set<String> permissions = new HashSet<>();
        // 如果是管理员直接返回
        if (user.getIsAdmin()) {
            permissions.add("admin");
            return permissions.stream().map(SystemRoleCodeVo::new)
                    .collect(Collectors.toList());
        }
        List<SystemRoleTb> roles = systemRoleMapper.queryRoleListByUserId(user.getId());
        permissions = roles.stream().flatMap(role -> role.getMenus().stream())
                .map(SystemMenuTb::getPermission)
                .filter(StringUtil::isNotBlank).collect(Collectors.toSet());
        return permissions.stream().map(SystemRoleCodeVo::new)
                .collect(Collectors.toList());
    }

    @Override
    public void verifyBindRelationByIds(Set<Long> ids) {
        if (systemUserMapper.getUserCountByRoleIds(ids) > 0) {
            throw new BadRequestException("所选角色存在用户关联，请解除关联再试！");
        }
    }

    @Override
    public List<SystemRoleTb> queryRoleListByMenuId(Long menuId) {
        return systemRoleMapper.queryRoleListByMenuId(menuId);
    }
}
