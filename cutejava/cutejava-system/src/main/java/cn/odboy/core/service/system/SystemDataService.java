package cn.odboy.core.service.system;

import cn.hutool.core.collection.CollUtil;
import cn.odboy.core.constant.system.SystemDataScopeEnum;
import cn.odboy.core.dal.dataobject.system.SystemDeptTb;
import cn.odboy.core.dal.dataobject.system.SystemRoleTb;
import cn.odboy.core.dal.dataobject.system.SystemUserTb;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 数据权限 Service
 *
 * @author odboy
 */
@Service
@RequiredArgsConstructor
public class SystemDataService {
    private final SystemRoleService systemRoleService;
    private final SystemDeptService systemDeptService;

    /**
     * 获取数据权限
     *
     * @param user /
     * @return /
     */
    public List<Long> queryDeptIdListByArgs(SystemUserTb user) {
        List<Long> deptIds = new ArrayList<>();
        // 查询用户角色
        List<SystemRoleTb> roleList = systemRoleService.queryRoleByUsersId(user.getId());
        // 获取对应的部门ID
        for (SystemRoleTb role : roleList) {
            SystemDataScopeEnum dataScopeEnum = SystemDataScopeEnum.find(role.getDataScope());
            switch (Objects.requireNonNull(dataScopeEnum)) {
                case THIS_LEVEL:
                    deptIds.add(user.getDept().getId());
                    break;
                case CUSTOMIZE:
                    deptIds.addAll(queryCustomDataPermissionList(deptIds, role));
                    break;
                default:
                    return new ArrayList<>();
            }
        }
        return deptIds.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 获取自定义的数据权限
     *
     * @param deptIds 部门ID
     * @param role    角色
     * @return 数据权限ID
     */
    private List<Long> queryCustomDataPermissionList(List<Long> deptIds, SystemRoleTb role) {
        List<SystemDeptTb> deptList = systemDeptService.queryDeptByRoleId(role.getId());
        for (SystemDeptTb dept : deptList) {
            deptIds.add(dept.getId());
            List<SystemDeptTb> deptChildren = systemDeptService.queryDeptByPid(dept.getId());
            if (CollUtil.isNotEmpty(deptChildren)) {
                deptIds.addAll(systemDeptService.queryChildDeptIdListByDeptIds(deptChildren));
            }
        }
        return deptIds;
    }
}
