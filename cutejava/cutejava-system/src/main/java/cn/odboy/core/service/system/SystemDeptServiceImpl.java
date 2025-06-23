package cn.odboy.core.service.system;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.odboy.base.CsResultVo;
import cn.odboy.core.constant.system.SystemDataScopeEnum;
import cn.odboy.core.dal.dataobject.system.SystemDeptTb;
import cn.odboy.core.dal.model.system.CreateSystemDeptArgs;
import cn.odboy.core.dal.model.system.QuerySystemDeptArgs;
import cn.odboy.core.dal.mysql.system.SystemDeptMapper;
import cn.odboy.core.dal.mysql.system.SystemRoleMapper;
import cn.odboy.core.dal.mysql.system.SystemUserMapper;
import cn.odboy.core.framework.permission.core.SecurityHelper;
import cn.odboy.exception.BadRequestException;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SystemDeptServiceImpl extends ServiceImpl<SystemDeptMapper, SystemDeptTb> implements SystemDeptService {
    private final SystemDeptMapper systemDeptMapper;
    private final SystemUserMapper systemUserMapper;
    private final SystemRoleMapper systemRoleMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDept(CreateSystemDeptArgs resources) {
        save(BeanUtil.copyProperties(resources, SystemDeptTb.class));
        // 清理缓存
        updateSubCnt(resources.getPid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyDept(SystemDeptTb resources) {
        // 旧的部门
        Long oldPid = queryDeptById(resources.getId()).getPid();
        Long newPid = resources.getPid();
        if (resources.getPid() != null && resources.getId().equals(resources.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        SystemDeptTb dept = getById(resources.getId());
        resources.setId(dept.getId());
        saveOrUpdate(resources);
        // 更新父节点中子节点数目
        updateSubCnt(oldPid);
        updateSubCnt(newPid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeDeptByIds(Set<SystemDeptTb> deptSet) {
        for (SystemDeptTb dept : deptSet) {
            systemDeptMapper.deleteById(dept.getId());
            updateSubCnt(dept.getPid());
        }
    }

    @Override
    public void downloadDeptExcel(List<SystemDeptTb> deptList, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SystemDeptTb dept : deptList) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("部门名称", dept.getName());
            map.put("部门状态", dept.getEnabled() ? "启用" : "停用");
            map.put("创建日期", dept.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }


    private void updateSubCnt(Long deptId) {
        if (deptId != null) {
            int count = systemDeptMapper.getDeptCountByPid(deptId);
            systemDeptMapper.updateSubCountById(count, deptId);
        }
    }

    /**
     * 筛选出 list 中没有父部门（即 pid 不在其他部门 id 列表中的部门）的部门列表
     */
    private List<SystemDeptTb> deduplication(List<SystemDeptTb> list) {
        List<SystemDeptTb> deptList = new ArrayList<>();
        // 使用 Set 存储所有部门的 id
        Set<Long> idSet = list.stream().map(SystemDeptTb::getId).collect(Collectors.toSet());
        // 遍历部门列表，筛选出没有父部门的部门
        for (SystemDeptTb dept : list) {
            if (!idSet.contains(dept.getPid())) {
                deptList.add(dept);
            }
        }
        return deptList;
    }

    @Override
    public List<SystemDeptTb> queryDeptList(QuerySystemDeptArgs criteria, Boolean isQuery) throws Exception {
        String dataScopeType = SecurityHelper.getDataScopeType();
        if (isQuery) {
            if (dataScopeType.equals(SystemDataScopeEnum.ALL.getValue())) {
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
        List<SystemDeptTb> list = systemDeptMapper.queryDeptListByArgs(criteria);
        // 如果为空，就代表为自定义权限或者本级权限，就需要去重，不理解可以注释掉，看查询结果
        if (StringUtil.isBlank(dataScopeType)) {
            return deduplication(list);
        }
        return list;
    }

    @Override
    public SystemDeptTb queryDeptById(Long id) {
        return systemDeptMapper.selectById(id);
    }

    @Override
    public List<SystemDeptTb> queryDeptListByPid(long pid) {
        return systemDeptMapper.queryDeptListByPid(pid);
    }

    @Override
    public Set<SystemDeptTb> queryDeptByRoleId(Long id) {
        return systemDeptMapper.queryDeptSetByRoleId(id);
    }

    @Override
    public Set<SystemDeptTb> queryRelationDeptSet(List<SystemDeptTb> menuList, Set<SystemDeptTb> deptSet) {
        for (SystemDeptTb dept : menuList) {
            deptSet.add(dept);
            List<SystemDeptTb> deptList = systemDeptMapper.queryDeptListByPid(dept.getId());
            if (CollUtil.isNotEmpty(deptList)) {
                queryRelationDeptSet(deptList, deptSet);
            }
        }
        return deptSet;
    }

    @Override
    public List<Long> queryChildDeptIdListByDeptIds(List<SystemDeptTb> deptList) {
        List<Long> list = new ArrayList<>();
        for (SystemDeptTb systemDeptTb : deptList) {
            if (systemDeptTb != null && systemDeptTb.getEnabled()) {
                List<SystemDeptTb> deptList1 = systemDeptMapper.queryDeptListByPid(systemDeptTb.getId());
                if (CollUtil.isNotEmpty(deptList1)) {
                    list.addAll(queryChildDeptIdListByDeptIds(deptList1));
                }
                list.add(systemDeptTb.getId());
            }
        }
        return list;
    }

    @Override
    public List<SystemDeptTb> querySuperiorDeptListByPid(SystemDeptTb dept, List<SystemDeptTb> deptList) {
        if (dept.getPid() == null) {
            deptList.addAll(systemDeptMapper.queryDeptListByPidIsNull());
            return deptList;
        }
        deptList.addAll(systemDeptMapper.queryDeptListByPid(dept.getPid()));
        return querySuperiorDeptListByPid(queryDeptById(dept.getPid()), deptList);
    }

    @Override
    public CsResultVo<Set<SystemDeptTb>> buildDeptTree(List<SystemDeptTb> deptList) {
        Set<SystemDeptTb> trees = new LinkedHashSet<>();
        Set<SystemDeptTb> deptSet = new LinkedHashSet<>();
        List<String> deptNames = deptList.stream().map(SystemDeptTb::getName).collect(Collectors.toList());
        boolean isChild;
        for (SystemDeptTb dept : deptList) {
            isChild = false;
            if (dept.getPid() == null) {
                trees.add(dept);
            }
            for (SystemDeptTb it : deptList) {
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
            } else if (dept.getPid() != null && !deptNames.contains(queryDeptById(dept.getPid()).getName())) {
                deptSet.add(dept);
            }
        }
        if (CollectionUtil.isEmpty(trees)) {
            trees = deptSet;
        }
        CsResultVo<Set<SystemDeptTb>> baseResult = new CsResultVo<>();
        baseResult.setContent(CollectionUtil.isEmpty(trees) ? deptSet : trees);
        baseResult.setTotalElements(deptSet.size());
        return baseResult;
    }

    @Override
    public void verifyBindRelationByIds(Set<SystemDeptTb> deptSet) {
        Set<Long> deptIds = deptSet.stream().map(SystemDeptTb::getId).collect(Collectors.toSet());
        if (systemUserMapper.getUserCountByDeptIds(deptIds) > 0) {
            throw new BadRequestException("所选部门存在用户关联，请解除后再试！");
        }
        if (systemRoleMapper.getRoleCountByDeptIds(deptIds) > 0) {
            throw new BadRequestException("所选部门存在角色关联，请解除后再试！");
        }
    }

    @Override
    public Set<SystemDeptTb> traverseDeptByIdWithPids(Set<Long> ids) {
        Set<SystemDeptTb> depts = new HashSet<>();
        for (Long id : ids) {
            // 根部门
            depts.add(queryDeptById(id));
            // 子部门
            List<SystemDeptTb> deptList = queryDeptListByPid(id);
            if (CollectionUtil.isNotEmpty(deptList)) {
                depts = queryRelationDeptSet(deptList, depts);
            }
        }
        return depts;
    }
}
