package cn.odboy.system.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.odboy.base.CsPageResultVo;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.system.dal.dataobject.SystemMenuTb;
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import cn.odboy.system.dal.dataobject.SystemUserTb;
import cn.odboy.system.dal.model.SystemCreateRoleArgs;
import cn.odboy.system.dal.model.SystemQueryRoleArgs;
import cn.odboy.system.dal.model.SystemRoleCodeVo;
import cn.odboy.system.dal.mysql.SystemRoleDeptMapper;
import cn.odboy.system.dal.mysql.SystemRoleMapper;
import cn.odboy.system.dal.mysql.SystemRoleMenuMapper;
import cn.odboy.system.dal.mysql.SystemUserMapper;
import cn.odboy.util.CsFileUtil;
import cn.odboy.util.CsPageUtil;
import cn.odboy.util.CsStringUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SystemRoleService {
    private final SystemRoleMapper systemRoleMapper;
    private final SystemRoleDeptMapper systemRoleDeptMapper;
    private final SystemRoleMenuMapper systemRoleMenuMapper;
    private final SystemUserMapper systemUserMapper;

    /**
     * 创建
     *
     * @param resources /
     */

    @Transactional(rollbackFor = Exception.class)
    public void saveRole(SystemCreateRoleArgs resources) {
        if (systemRoleMapper.getRoleByName(resources.getName()) != null) {
            throw new BadRequestException("角色名称已存在");
        }
        systemRoleMapper.insert(BeanUtil.copyProperties(resources, SystemRoleTb.class));
        // 判断是否有部门数据, 若有, 则需创建关联
        if (CollectionUtil.isNotEmpty(resources.getDepts())) {
            systemRoleDeptMapper.batchInsertRoleDept(resources.getDepts(), resources.getId());
        }
    }

    /**
     * 修改
     *
     * @param resources /
     */

    @Transactional(rollbackFor = Exception.class)
    public void modifyRoleById(SystemRoleTb resources) {
        SystemRoleTb role = systemRoleMapper.selectById(resources.getId());
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
        systemRoleMapper.insertOrUpdate(role);
        // 删除关联部门数据
        systemRoleDeptMapper.batchDeleteRoleDept(Collections.singleton(resources.getId()));
        // 判断是否有部门数据, 若有, 则需更新关联
        if (CollectionUtil.isNotEmpty(resources.getDepts())) {
            systemRoleDeptMapper.batchInsertRoleDept(resources.getDepts(), resources.getId());
        }
    }

    /**
     * 修改绑定的菜单
     *
     * @param role /
     */

    public void modifyBindMenuById(SystemRoleTb role) {
        // 更新菜单
        systemRoleMenuMapper.deleteRoleMenuByRoleId(role.getId());
        // 判断是否为空
        if (CollUtil.isNotEmpty(role.getMenus())) {
            systemRoleMenuMapper.batchInsertRoleMenu(role.getMenus(), role.getId());
        }
    }

    /**
     * 删除
     *
     * @param ids /
     */

    @Transactional(rollbackFor = Exception.class)
    public void removeRoleByIds(Set<Long> ids) {
        systemRoleMapper.deleteByIds(ids);
        // 删除角色部门关联数据、角色菜单关联数据
        systemRoleDeptMapper.batchDeleteRoleDept(ids);
        systemRoleMenuMapper.batchDeleteRoleMenu(ids);
    }

    /**
     * 导出数据
     *
     * @param roles    待导出的数据
     * @param response /
     * @throws IOException
     */

    public void exportRoleExcel(List<SystemRoleTb> roles, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SystemRoleTb role : roles) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("角色名称", role.getName());
            map.put("角色级别", role.getLevel());
            map.put("描述", role.getDescription());
            map.put("创建日期", role.getCreateTime());
            list.add(map);
        }
        CsFileUtil.downloadExcel(list, response);
    }

    /**
     * 查询全部角色
     *
     * @return
     */

    public List<SystemRoleTb> queryAllRole() {
        return systemRoleMapper.selectAllRole();
    }

    /**
     * 根据条件查询全部角色
     *
     * @param criteria 条件
     * @return
     */

    public List<SystemRoleTb> queryRoleByArgs(SystemQueryRoleArgs criteria) {
        return systemRoleMapper.selectRoleByArgs(criteria);
    }

    /**
     * 分页查询角色
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return
     */

    public CsPageResultVo<List<SystemRoleTb>> queryRoleByArgs(SystemQueryRoleArgs criteria, Page<Object> page) {
        criteria.setOffset(page.offset());
        List<SystemRoleTb> roles = systemRoleMapper.selectRoleByArgs(criteria);
        Long total = systemRoleMapper.countRoleByArgs(criteria);
        return CsPageUtil.toPage(roles, total);
    }

    /**
     * 根据用户ID查询
     *
     * @param userId 用户ID
     * @return /
     */

    public List<SystemRoleTb> queryRoleByUsersId(Long userId) {
        return systemRoleMapper.selectRoleByUserId(userId);
    }

    /**
     * 根据角色查询角色级别
     *
     * @param roles /
     * @return /
     */

    public Integer getDeptLevelByRoles(Set<SystemRoleTb> roles) {
        if (CollUtil.isEmpty(roles)) {
            return Integer.MAX_VALUE;
        }
        Set<SystemRoleTb> roleSet = new HashSet<>();
        for (SystemRoleTb role : roles) {
            roleSet.add(getRoleById(role.getId()));
        }
        return Collections.min(roleSet.stream().map(SystemRoleTb::getLevel).collect(Collectors.toList()));
    }

    /**
     * 获取用户权限信息
     *
     * @param user 用户信息
     * @return 权限信息
     */

    public List<SystemRoleCodeVo> buildUserRolePermissions(SystemUserTb user) {
        Set<String> permissions = new HashSet<>();
        // 如果是管理员直接返回
        if (user.getIsAdmin()) {
            permissions.add("admin");
            return permissions.stream().map(SystemRoleCodeVo::new).collect(Collectors.toList());
        }
        List<SystemRoleTb> roles = systemRoleMapper.selectRoleByUserId(user.getId());
        permissions = roles.stream().flatMap(role -> role.getMenus().stream()).map(SystemMenuTb::getPermission).filter(CsStringUtil::isNotBlank)
            .collect(Collectors.toSet());
        return permissions.stream().map(SystemRoleCodeVo::new).collect(Collectors.toList());
    }

    /**
     * 验证是否被用户关联
     *
     * @param ids /
     */

    public void verifyBindRelationByIds(Set<Long> ids) {
        if (systemUserMapper.countUserByRoleIds(ids) > 0) {
            throw new BadRequestException("所选角色存在用户关联, 请解除关联再试！");
        }
    }

    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */
    public SystemRoleTb getRoleById(Long id) {
        return systemRoleMapper.selectById(id);
    }
}
