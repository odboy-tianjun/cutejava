package cn.odboy.core.service.system;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.odboy.core.dal.redis.system.SystemUserJwtService;
import cn.odboy.core.dal.redis.system.SystemRedisKey;
import cn.odboy.core.dal.dataobject.system.SystemRoleTb;
import cn.odboy.core.dal.dataobject.system.SystemUserTb;
import cn.odboy.core.dal.mysql.system.SystemRoleDeptMapper;
import cn.odboy.core.dal.mysql.system.SystemRoleMapper;
import cn.odboy.core.dal.mysql.system.SystemRoleMenuMapper;
import cn.odboy.core.dal.mysql.system.SystemUserMapper;
import cn.odboy.core.dal.model.system.CreateSystemRoleArgs;
import cn.odboy.exception.EntityExistException;
import cn.odboy.redis.RedisHelper;
import cn.odboy.util.FileUtil;
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
    private final SystemRoleMapper roleMapper;
    private final SystemRoleDeptMapper roleDeptMapper;
    private final SystemRoleMenuMapper roleMenuMapper;
    private final SystemUserMapper userMapper;
    private final SystemUserJwtService systemUserJwtService;
    private final RedisHelper redisHelper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(CreateSystemRoleArgs resources) {
        if (roleMapper.getRoleByName(resources.getName()) != null) {
            throw new EntityExistException(SystemRoleTb.class, "name", resources.getName());
        }
        save(BeanUtil.copyProperties(resources, SystemRoleTb.class));
        // 判断是否有部门数据，若有，则需创建关联
        if (CollectionUtil.isNotEmpty(resources.getDepts())) {
            roleDeptMapper.insertBatchWithRoleId(resources.getDepts(), resources.getId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyRoleById(SystemRoleTb resources) {
        SystemRoleTb role = getById(resources.getId());
        SystemRoleTb role1 = roleMapper.getRoleByName(resources.getName());
        if (role1 != null && !role1.getId().equals(role.getId())) {
            throw new EntityExistException(SystemRoleTb.class, "name", resources.getName());
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
            roleDeptMapper.insertBatchWithRoleId(resources.getDepts(), resources.getId());
        }
        // 更新相关缓存
        delCaches(role.getId(), null);
    }

    @Override
    public void modifyBindMenuById(SystemRoleTb role) {
        List<SystemUserTb> users = userMapper.queryUserListByRoleId(role.getId());
        // 更新菜单
        roleMenuMapper.deleteByRoleId(role.getId());
        // 判断是否为空
        if (CollUtil.isNotEmpty(role.getMenus())) {
            roleMenuMapper.insertBatchWithRoleId(role.getMenus(), role.getId());
        }
        // 更新缓存
        delCaches(role.getId(), users);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRoleByIds(Set<Long> ids) {
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

    /**
     * 清理缓存
     *
     * @param id /
     */
    public void delCaches(Long id, List<SystemUserTb> users) {
        users = CollectionUtil.isEmpty(users) ? userMapper.queryUserListByRoleId(id) : users;
        if (CollectionUtil.isNotEmpty(users)) {
            users.forEach(item -> systemUserJwtService.cleanUserJwtModelCacheByUsername(item.getUsername()));
            Set<Long> userIds = users.stream().map(SystemUserTb::getId).collect(Collectors.toSet());
            redisHelper.delByKeys(SystemRedisKey.DATA_USER, userIds);
            redisHelper.delByKeys(SystemRedisKey.MENU_USER, userIds);
            redisHelper.delByKeys(SystemRedisKey.ROLE_AUTH, userIds);
            redisHelper.delByKeys(SystemRedisKey.ROLE_USER, userIds);
        }
        redisHelper.del(SystemRedisKey.ROLE_ID + id);
    }
}
