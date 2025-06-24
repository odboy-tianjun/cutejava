package cn.odboy.core.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.odboy.base.CsResultVo;
import cn.odboy.core.constant.SystemDataScopeEnum;
import cn.odboy.core.dal.dataobject.SystemDeptTb;
import cn.odboy.core.dal.model.CreateSystemDeptArgs;
import cn.odboy.core.dal.model.QuerySystemDeptArgs;
import cn.odboy.core.dal.mysql.SystemDeptMapper;
import cn.odboy.core.dal.mysql.SystemRoleMapper;
import cn.odboy.core.dal.mysql.SystemUserMapper;
import cn.odboy.core.framework.permission.core.SecurityHelper;
import cn.odboy.exception.BadRequestException;
import cn.odboy.util.ClassUtil;
import cn.odboy.util.FileUtil;
import cn.odboy.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class SystemDeptService {
    private final SystemDeptMapper systemDeptMapper;
    private final SystemUserMapper systemUserMapper;
    private final SystemRoleMapper systemRoleMapper;
    private final SystemDeptService systemDeptService;

    /**
     * 创建
     *
     * @param resources /
     */

    @Transactional(rollbackFor = Exception.class)
    public void saveDept(CreateSystemDeptArgs resources) {
        systemDeptMapper.insert(BeanUtil.copyProperties(resources, SystemDeptTb.class));
        systemDeptService.updateDeptSubCnt(resources.getPid());
    }

    /**
     * 编辑
     *
     * @param resources /
     */

    @Transactional(rollbackFor = Exception.class)
    public void modifyDept(SystemDeptTb resources) {
        // 旧的部门
        Long oldPid = queryDeptById(resources.getId()).getPid();
        Long newPid = resources.getPid();
        if (resources.getPid() != null && resources.getId().equals(resources.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        SystemDeptTb dept = systemDeptMapper.selectById(resources.getId());
        resources.setId(dept.getId());
        systemDeptMapper.insertOrUpdate(resources);
        systemDeptService.updateDeptSubCnt(oldPid);
        systemDeptService.updateDeptSubCnt(newPid);
    }

    /**
     * 删除
     *
     * @param deptSet /
     */

    @Transactional(rollbackFor = Exception.class)
    public void removeDeptByIds(Set<SystemDeptTb> deptSet) {
        for (SystemDeptTb dept : deptSet) {
            systemDeptMapper.deleteById(dept.getId());
            systemDeptService.updateDeptSubCnt(dept.getPid());
        }
    }

    /**
     * 导出数据
     *
     * @param deptList 待导出的数据
     * @param response /
     * @throws IOException /
     */
    public void exportDeptExcel(List<SystemDeptTb> deptList, HttpServletResponse response) throws IOException {
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


    /**
     * 更新父节点中子节点数目
     *
     * @param deptId /
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateDeptSubCnt(Long deptId) {
        if (deptId != null) {
            int count = systemDeptMapper.countDeptByPid(deptId);
            systemDeptMapper.updateDeptSubCountById(count, deptId);
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

    /**
     * 查询所有数据
     *
     * @param criteria 条件
     * @param isQuery  /
     * @return /
     * @throws Exception /
     */

    public List<SystemDeptTb> queryAllDept(QuerySystemDeptArgs criteria, Boolean isQuery) throws Exception {
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
        List<SystemDeptTb> list = systemDeptMapper.selectDeptByArgs(criteria);
        // 如果为空，就代表为自定义权限或者本级权限，就需要去重，不理解可以注释掉，看查询结果
        if (StringUtil.isBlank(dataScopeType)) {
            return deduplication(list);
        }
        return list;
    }

    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */

    public SystemDeptTb queryDeptById(Long id) {
        return systemDeptMapper.selectById(id);
    }

    /**
     * 根据PID查询
     *
     * @param pid /
     * @return /
     */

    public List<SystemDeptTb> queryDeptByPid(long pid) {
        return systemDeptMapper.selectDeptByPid(pid);
    }

    /**
     * 根据角色ID查询
     *
     * @param id /
     * @return /
     */

    public List<SystemDeptTb> queryDeptByRoleId(Long id) {
        return systemDeptMapper.selectDeptByRoleId(id);
    }

    /**
     * 获取部门下所有关联的部门
     *
     * @param deptTbList /
     * @param depts      /
     * @return /
     */

    public Set<SystemDeptTb> queryRelationDeptSet(List<SystemDeptTb> deptTbList, Set<SystemDeptTb> depts) {
        for (SystemDeptTb dept : deptTbList) {
            depts.add(dept);
            List<SystemDeptTb> deptList = systemDeptMapper.selectDeptByPid(dept.getId());
            if (CollUtil.isNotEmpty(deptList)) {
                queryRelationDeptSet(deptList, depts);
            }
        }
        return depts;
    }

    /**
     * 获取
     *
     * @param deptList 、
     * @return 、
     */

    public List<Long> queryChildDeptIdListByDeptIds(List<SystemDeptTb> deptList) {
        List<Long> list = new ArrayList<>();
        for (SystemDeptTb systemDeptTb : deptList) {
            if (systemDeptTb != null && systemDeptTb.getEnabled()) {
                List<SystemDeptTb> deptList1 = systemDeptMapper.selectDeptByPid(systemDeptTb.getId());
                if (CollUtil.isNotEmpty(deptList1)) {
                    list.addAll(queryChildDeptIdListByDeptIds(deptList1));
                }
                list.add(systemDeptTb.getId());
            }
        }
        return list;
    }

    /**
     * 根据ID获取同级与上级数据
     *
     * @param dept     /
     * @param deptList /
     * @return /
     */

    public List<SystemDeptTb> querySuperiorDeptListByPid(SystemDeptTb dept, List<SystemDeptTb> deptList) {
        if (dept.getPid() == null) {
            deptList.addAll(systemDeptMapper.selectDeptByPidIsNull());
            return deptList;
        }
        deptList.addAll(systemDeptMapper.selectDeptByPid(dept.getPid()));
        return querySuperiorDeptListByPid(queryDeptById(dept.getPid()), deptList);
    }

    /**
     * 构建树形数据
     *
     * @param deptList /
     * @return /
     */

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

    /**
     * 验证是否被角色或用户关联
     *
     * @param deptSet /
     */

    public void verifyBindRelationByIds(Set<SystemDeptTb> deptSet) {
        Set<Long> deptIds = deptSet.stream().map(SystemDeptTb::getId).collect(Collectors.toSet());
        if (systemUserMapper.countUserByDeptIds(deptIds) > 0) {
            throw new BadRequestException("所选部门存在用户关联，请解除后再试！");
        }
        if (systemRoleMapper.countRoleByDeptIds(deptIds) > 0) {
            throw new BadRequestException("所选部门存在角色关联，请解除后再试！");
        }
    }

    /**
     * 遍历所有部门和子部门
     *
     * @param ids /
     */

    public Set<SystemDeptTb> traverseDeptByIdWithPids(Set<Long> ids) {
        Set<SystemDeptTb> depts = new HashSet<>();
        for (Long id : ids) {
            // 根部门
            depts.add(queryDeptById(id));
            // 子部门
            List<SystemDeptTb> deptList = queryDeptByPid(id);
            if (CollectionUtil.isNotEmpty(deptList)) {
                depts = queryRelationDeptSet(deptList, depts);
            }
        }
        return depts;
    }
}
