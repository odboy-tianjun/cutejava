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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.base.KitPageResult;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.system.dal.dataobject.SystemDeptTb;
import cn.odboy.system.dal.dataobject.SystemMenuTb;
import cn.odboy.system.dal.dataobject.SystemRoleDeptTb;
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import cn.odboy.system.dal.dataobject.SystemUserRoleTb;
import cn.odboy.system.dal.dataobject.SystemUserTb;
import cn.odboy.system.dal.model.SystemCreateRoleArgs;
import cn.odboy.system.dal.model.SystemQueryRoleArgs;
import cn.odboy.system.dal.model.SystemRoleCodeVo;
import cn.odboy.system.dal.model.SystemRoleVo;
import cn.odboy.system.dal.mysql.SystemRoleDeptMapper;
import cn.odboy.system.dal.mysql.SystemRoleMapper;
import cn.odboy.system.dal.mysql.SystemUserRoleMapper;
import cn.odboy.util.KitFileUtil;
import cn.odboy.util.KitPageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemRoleService {
    @Autowired private SystemRoleMapper systemRoleMapper;
    @Autowired private SystemRoleMenuService systemRoleMenuService;
    @Autowired private SystemRoleDeptService systemRoleDeptService;
    @Autowired private SystemRoleDeptMapper systemRoleDeptMapper;
    @Autowired private SystemUserRoleMapper systemUserRoleMapper;
    @Autowired private SystemUserService systemUserService;

    /**
     * 创建
     *
     * @param args /
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(SystemCreateRoleArgs args) {
        if (this.getRoleByName(args.getName()) != null) {
            throw new BadRequestException("角色名称已存在");
        }
        systemRoleMapper.insert(BeanUtil.copyProperties(args, SystemRoleTb.class));
        // 判断是否有部门数据, 若有, 则需创建关联
        if (CollectionUtil.isNotEmpty(args.getDepts())) {
            systemRoleDeptService.batchInsertRoleDept(args.getDepts(), args.getId());
        }
    }

    private SystemRoleTb getRoleByName(String name) {
        LambdaQueryWrapper<SystemRoleTb> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemRoleTb::getName, name);
        return systemRoleMapper.selectOne(wrapper);
    }

    /**
     * 修改
     *
     * @param args /
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleById(SystemRoleVo args) {
        SystemRoleVo role = BeanUtil.copyProperties(systemRoleMapper.selectById(args.getId()), SystemRoleVo.class);
        SystemRoleTb role1 = this.getRoleByName(args.getName());
        if (role1 != null && !role1.getId().equals(role.getId())) {
            throw new BadRequestException("角色名称已存在");
        }
        role.setName(args.getName());
        role.setDescription(args.getDescription());
        role.setDataScope(args.getDataScope());
        role.setDepts(args.getDepts());
        role.setLevel(args.getLevel());
        // 更新
        systemRoleMapper.insertOrUpdate(role);
        // 删除关联部门数据
        systemRoleDeptService.batchDeleteRoleDept(Collections.singleton(args.getId()));
        // 判断是否有部门数据, 若有, 则需更新关联
        if (CollectionUtil.isNotEmpty(args.getDepts())) {
            systemRoleDeptService.batchInsertRoleDept(args.getDepts(), args.getId());
        }
    }

    /**
     * 修改绑定的菜单
     *
     * @param role /
     */
    public void updateBindMenuById(SystemRoleVo role) {
        // 更新菜单
        systemRoleMenuService.deleteRoleMenuByRoleId(role.getId());
        // 判断是否为空
        if (CollUtil.isNotEmpty(role.getMenus())) {
            systemRoleMenuService.batchInsertRoleMenu(role.getMenus(), role.getId());
        }
    }

    /**
     * 删除
     *
     * @param ids /
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleByIds(Set<Long> ids) {
        systemRoleMapper.deleteByIds(ids);
        // 删除角色部门关联数据、角色菜单关联数据
        systemRoleDeptService.batchDeleteRoleDept(ids);
        systemRoleMenuService.batchDeleteRoleMenu(ids);
    }

    /**
     * 导出数据
     *
     * @param roles    待导出的数据
     * @param response /
     * @throws IOException
     */
    public void exportRoleExcel(List<SystemRoleVo> roles, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SystemRoleVo role : roles) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("角色名称", role.getName());
            map.put("角色级别", role.getLevel());
            map.put("描述", role.getDescription());
            map.put("创建日期", role.getCreateTime());
            list.add(map);
        }
        KitFileUtil.downloadExcel(list, response);
    }

    /**
     * 查询全部角色
     */
    public List<SystemRoleTb> listAllRole() {
        LambdaQueryWrapper<SystemRoleTb> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(SystemRoleTb::getLevel);
        return systemRoleMapper.selectList(wrapper);
    }

    /**
     * 根据条件查询全部角色
     *
     * @param args 条件
     * @return
     */
    public List<SystemRoleVo> queryRoleByArgs(SystemQueryRoleArgs args) {
        // 查询角色基本信息
        LambdaQueryWrapper<SystemRoleTb> wrapper = new LambdaQueryWrapper<>();
        if (args != null) {
            wrapper.and(StrUtil.isNotBlank(args.getBlurry()), c -> c.like(SystemRoleTb::getName, args.getBlurry()).or()
                .like(SystemRoleTb::getDescription, args.getBlurry()));
            if (CollUtil.isNotEmpty(args.getCreateTime()) && args.getCreateTime().size() >= 2) {
                wrapper.between(SystemRoleTb::getCreateTime, args.getCreateTime().get(0), args.getCreateTime().get(1));
            }
        }
        wrapper.orderByAsc(SystemRoleTb::getLevel);
        if (args != null && args.getSize() != null) {
            Page<SystemRoleTb> rolePage = new Page<>(args.getPage(), args.getSize());
            Page<SystemRoleTb> page = systemRoleMapper.selectPage(rolePage, wrapper);
            List<SystemRoleTb> roles = page.getRecords();
            return roles.stream().map(this::convertToRoleVo).collect(Collectors.toList());
        } else {
            List<SystemRoleTb> roles = systemRoleMapper.selectList(wrapper);
            return roles.stream().map(this::convertToRoleVo).collect(Collectors.toList());
        }
    }

    /**
     * 分页查询角色
     *
     * @param args 条件
     * @param page 分页参数
     * @return
     */
    public KitPageResult<SystemRoleVo> searchRoleByArgs(SystemQueryRoleArgs args, Page<Object> page) {
        args.setOffset(page.offset());
        List<SystemRoleVo> roles = this.queryRoleByArgs(args);
        Long total = this.countRoleByArgs(args);
        return KitPageUtil.toPage(roles, total);
    }

    /**
     * 根据用户ID查询
     *
     * @param userId 用户ID
     * @return /
     */
    public List<SystemRoleVo> queryRoleByUsersId(Long userId) {
        // 查询用户角色关联
        LambdaQueryWrapper<SystemUserRoleTb> userRoleWrapper = new LambdaQueryWrapper<>();
        userRoleWrapper.eq(SystemUserRoleTb::getUserId, userId);
        List<SystemUserRoleTb> userRoles = systemUserRoleMapper.selectList(userRoleWrapper);
        if (CollUtil.isEmpty(userRoles)) {
            return new ArrayList<>();
        }
        Set<Long> roleIds = userRoles.stream().map(SystemUserRoleTb::getRoleId).collect(Collectors.toSet());
        List<SystemRoleTb> roles = systemRoleMapper.selectByIds(roleIds);
        return roles.stream().map(this::convertToRoleVo).collect(Collectors.toList());
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
        List<SystemRoleVo> roles = this.queryRoleByUsersId(user.getId());
        permissions = roles.stream().flatMap(role -> role.getMenus().stream()).map(SystemMenuTb::getPermission)
            .filter(StrUtil::isNotBlank).collect(Collectors.toSet());
        return permissions.stream().map(SystemRoleCodeVo::new).collect(Collectors.toList());
    }

    /**
     * 验证是否被用户关联
     *
     * @param ids /
     */
    public void verifyBindRelationByIds(Set<Long> ids) {
        if (systemUserService.countUserByRoleIds(ids) > 0) {
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

    /**
     * 根据部门ID统计角色数量
     *
     * @param deptIds 部门ID集合
     * @return /
     */
    public Long countRoleByDeptIds(Set<Long> deptIds) {
        if (CollUtil.isEmpty(deptIds)) {
            return 0L;
        }

        LambdaQueryWrapper<SystemRoleDeptTb> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SystemRoleDeptTb::getDeptId, deptIds);
        return systemRoleDeptMapper.selectCount(wrapper);
    }

    /**
     * 转换为SystemRoleVo并关联查询菜单和部门信息
     *
     * @param role 角色基本信息
     * @return 包含关联信息的SystemRoleVo
     */
    private SystemRoleVo convertToRoleVo(SystemRoleTb role) {
        if (role == null) {
            return null;
        }

        SystemRoleVo roleVo = BeanUtil.copyProperties(role, SystemRoleVo.class);
        // 查询关联的菜单信息
        Set<SystemMenuTb> menus = systemRoleMenuService.queryMenuByRoleIds(Collections.singleton(role.getId()));
        roleVo.setMenus(menus);

        // 查询关联的部门信息
        List<SystemDeptTb> depts = systemRoleDeptService.listDeptByRoleId(role.getId());
        roleVo.setDepts(new LinkedHashSet<>(depts));

        return roleVo;
    }

    public Long countRoleByArgs(SystemQueryRoleArgs args) {
        LambdaQueryWrapper<SystemRoleTb> wrapper = new LambdaQueryWrapper<>();
        if (args != null) {
            wrapper.and(StrUtil.isNotBlank(args.getBlurry()), c -> c.like(SystemRoleTb::getName, args.getBlurry()).or()
                .like(SystemRoleTb::getDescription, args.getBlurry()));
            if (CollUtil.isNotEmpty(args.getCreateTime()) && args.getCreateTime().size() >= 2) {
                wrapper.between(SystemRoleTb::getCreateTime, args.getCreateTime().get(0), args.getCreateTime().get(1));
            }
        }
        return systemRoleMapper.selectCount(wrapper);
    }
}
