package cn.odboy.application.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.odboy.application.system.mapper.DeptMapper;
import cn.odboy.application.system.mapper.RoleMapper;
import cn.odboy.application.system.mapper.UserMapper;
import cn.odboy.application.system.service.DeptService;
import cn.odboy.base.BaseResult;
import cn.odboy.constant.DataScopeEnum;
import cn.odboy.constant.SystemRedisKey;
import cn.odboy.context.SecurityHelper;
import cn.odboy.exception.BadRequestException;
import cn.odboy.model.system.domain.Dept;
import cn.odboy.model.system.domain.User;
import cn.odboy.model.system.request.CreateDeptRequest;
import cn.odboy.model.system.request.QueryDeptRequest;
import cn.odboy.redis.RedisHelper;
import cn.odboy.util.ClassUtil;
import cn.odboy.util.FileUtil;
import cn.odboy.util.StringUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {
    private final DeptMapper deptMapper;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final RedisHelper redisHelper;

    @Override
    public List<Dept> describeDeptList(QueryDeptRequest criteria, Boolean isQuery) throws Exception {
        String dataScopeType = SecurityHelper.getDataScopeType();
        if (isQuery) {
            if (dataScopeType.equals(DataScopeEnum.ALL.getValue())) {
                criteria.setPidIsNull(true);
            }
            List<Field> fields = ClassUtil.getAllFields(criteria.getClass(), new ArrayList<>());
            List<String> fieldNames = new ArrayList<>() {{
                add("pidIsNull");
                add("enabled");
            }};
            for (Field field : fields) {
                // 设置对象的访问权限，保证对private的属性的访问
                field.setAccessible(true);
                Object val = field.get(criteria);
                if (fieldNames.contains(field.getName())) {
                    continue;
                }
                if (ObjectUtil.isNotNull(val)) {
                    criteria.setPidIsNull(null);
                    break;
                }
            }
        }
        // 数据权限
        criteria.setIds(SecurityHelper.getCurrentUserDataScope());
        List<Dept> list = deptMapper.queryDeptListByArgs(criteria);
        // 如果为空，就代表为自定义权限或者本级权限，就需要去重，不理解可以注释掉，看查询结果
        if (StringUtil.isBlank(dataScopeType)) {
            return deduplication(list);
        }
        return list;
    }

    @Override
    public Dept describeDeptById(Long id) {
        String key = SystemRedisKey.DEPT_ID + id;
        Dept dept = redisHelper.get(key, Dept.class);
        if (dept == null) {
            dept = deptMapper.selectById(id);
            redisHelper.set(key, dept, 1, TimeUnit.DAYS);
        }
        return dept;
    }

    @Override
    public List<Dept> describeDeptListByPid(long pid) {
        return deptMapper.queryDeptListByPid(pid);
    }

    @Override
    public Set<Dept> describeDeptByRoleId(Long id) {
        return deptMapper.queryDeptSetByRoleId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDept(CreateDeptRequest resources) {
        save(BeanUtil.copyProperties(resources, Dept.class));
        // 清理缓存
        updateSubCnt(resources.getPid());
        // 清理自定义角色权限的DataScope缓存
        delCaches(resources.getPid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyDept(Dept resources) {
        // 旧的部门
        Long oldPid = describeDeptById(resources.getId()).getPid();
        Long newPid = resources.getPid();
        if (resources.getPid() != null && resources.getId().equals(resources.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        Dept dept = getById(resources.getId());
        resources.setId(dept.getId());
        saveOrUpdate(resources);
        // 更新父节点中子节点数目
        updateSubCnt(oldPid);
        updateSubCnt(newPid);
        // 清理缓存
        delCaches(resources.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeDeptByIds(Set<Dept> deptSet) {
        for (Dept dept : deptSet) {
            // 清理缓存
            delCaches(dept.getId());
            deptMapper.deleteById(dept.getId());
            updateSubCnt(dept.getPid());
        }
    }

    @Override
    public void downloadDeptExcel(List<Dept> deptList, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Dept dept : deptList) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("部门名称", dept.getName());
            map.put("部门状态", dept.getEnabled() ? "启用" : "停用");
            map.put("创建日期", dept.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public Set<Dept> describeRelationDeptSet(List<Dept> menuList, Set<Dept> deptSet) {
        for (Dept dept : menuList) {
            deptSet.add(dept);
            List<Dept> deptList = deptMapper.queryDeptListByPid(dept.getId());
            if (CollUtil.isNotEmpty(deptList)) {
                describeRelationDeptSet(deptList, deptSet);
            }
        }
        return deptSet;
    }

    @Override
    public List<Long> describeChildDeptIdListByDeptIds(List<Dept> deptList) {
        List<Long> list = new ArrayList<>();
        deptList.forEach(dept -> {
                    if (dept != null && dept.getEnabled()) {
                        List<Dept> deptList1 = deptMapper.queryDeptListByPid(dept.getId());
                        if (CollUtil.isNotEmpty(deptList1)) {
                            list.addAll(describeChildDeptIdListByDeptIds(deptList1));
                        }
                        list.add(dept.getId());
                    }
                }
        );
        return list;
    }

    @Override
    public List<Dept> describeSuperiorDeptListByPid(Dept dept, List<Dept> deptList) {
        if (dept.getPid() == null) {
            deptList.addAll(deptMapper.queryDeptListByPidIsNull());
            return deptList;
        }
        deptList.addAll(deptMapper.queryDeptListByPid(dept.getPid()));
        return describeSuperiorDeptListByPid(describeDeptById(dept.getPid()), deptList);
    }

    @Override
    public BaseResult<Object> buildDeptTree(List<Dept> deptList) {
        Set<Dept> trees = new LinkedHashSet<>();
        Set<Dept> deptSet = new LinkedHashSet<>();
        List<String> deptNames = deptList.stream().map(Dept::getName).collect(Collectors.toList());
        boolean isChild;
        for (Dept dept : deptList) {
            isChild = false;
            if (dept.getPid() == null) {
                trees.add(dept);
            }
            for (Dept it : deptList) {
                if (it.getPid() != null && dept.getId().equals(it.getPid())) {
                    isChild = true;
                    if (dept.getChildren() == null) {
                        dept.setChildren(new ArrayList<>());
                    }
                    dept.getChildren().add(it);
                }
            }
            if (isChild) {
                deptSet.add(dept);
            } else if (dept.getPid() != null && !deptNames.contains(describeDeptById(dept.getPid()).getName())) {
                deptSet.add(dept);
            }
        }
        if (CollectionUtil.isEmpty(trees)) {
            trees = deptSet;
        }
        BaseResult<Object> baseResult = new BaseResult<>();
        baseResult.setContent(CollectionUtil.isEmpty(trees) ? deptSet : trees);
        baseResult.setTotalElements(deptSet.size());
        return baseResult;
    }

    @Override
    public void verifyBindRelationByIds(Set<Dept> deptSet) {
        Set<Long> deptIds = deptSet.stream().map(Dept::getId).collect(Collectors.toSet());
        if (userMapper.getCountByDeptIds(deptIds) > 0) {
            throw new BadRequestException("所选部门存在用户关联，请解除后再试！");
        }
        if (roleMapper.getRoleCountByDeptIds(deptIds) > 0) {
            throw new BadRequestException("所选部门存在角色关联，请解除后再试！");
        }
    }

    @Override
    public void traverseDeptByIdWithPids(Set<Long> ids, Set<Dept> depts) {
        for (Long id : ids) {
            // 根部门
            depts.add(describeDeptById(id));
            // 子部门
            List<Dept> deptList = describeDeptListByPid(id);
            if (CollectionUtil.isNotEmpty(deptList)) {
                depts = describeRelationDeptSet(deptList, depts);
            }
        }
    }

    private void updateSubCnt(Long deptId) {
        if (deptId != null) {
            int count = deptMapper.getDeptCountByPid(deptId);
            deptMapper.updateSubCountById(count, deptId);
        }
    }

    /**
     * 筛选出 list 中没有父部门（即 pid 不在其他部门 id 列表中的部门）的部门列表
     */
    private List<Dept> deduplication(List<Dept> list) {
        List<Dept> deptList = new ArrayList<>();
        // 使用 Set 存储所有部门的 id
        Set<Long> idSet = list.stream().map(Dept::getId).collect(Collectors.toSet());
        // 遍历部门列表，筛选出没有父部门的部门
        for (Dept dept : list) {
            if (!idSet.contains(dept.getPid())) {
                deptList.add(dept);
            }
        }
        return deptList;
    }

    /**
     * 清理缓存
     *
     * @param id /
     */
    public void delCaches(Long id) {
        List<User> users = userMapper.queryUserListByDeptId(id);
        // 删除数据权限
        redisHelper.delByKeys(SystemRedisKey.DATA_USER, users.stream().map(User::getId).collect(Collectors.toSet()));
        redisHelper.del(SystemRedisKey.DEPT_ID + id);
    }
}
