package cn.odboy.application.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.odboy.application.system.service.DataService;
import cn.odboy.application.system.service.DeptService;
import cn.odboy.application.system.service.RoleService;
import cn.odboy.constant.DataScopeEnum;
import cn.odboy.constant.SystemRedisKey;
import cn.odboy.model.system.domain.Dept;
import cn.odboy.model.system.domain.Role;
import cn.odboy.model.system.domain.User;
import cn.odboy.redis.RedisHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 数据权限服务实现
 */
@Service
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {
    private final RedisHelper redisHelper;
    private final RoleService roleService;
    private final DeptService deptService;

    /**
     * 用户角色和用户部门改变时需清理缓存
     *
     * @param user /
     * @return /
     */
    @Override
    public List<Long> describeDeptIdListByUserIdWithDeptId(User user) {
        String key = SystemRedisKey.DATA_USER + user.getId();
        List<Long> ids = redisHelper.getList(key, Long.class);
        if (CollUtil.isEmpty(ids)) {
            Set<Long> deptIds = new HashSet<>();
            // 查询用户角色
            List<Role> roleList = roleService.selectRoleByUsersId(user.getId());
            // 获取对应的部门ID
            for (Role role : roleList) {
                DataScopeEnum dataScopeEnum = DataScopeEnum.find(role.getDataScope());
                switch (Objects.requireNonNull(dataScopeEnum)) {
                    case THIS_LEVEL:
                        deptIds.add(user.getDept().getId());
                        break;
                    case CUSTOMIZE:
                        deptIds.addAll(getCustomize(deptIds, role));
                        break;
                    default:
                        return new ArrayList<>();
                }
            }
            ids = new ArrayList<>(deptIds);
            redisHelper.set(key, ids, 1, TimeUnit.DAYS);
        }
        return ids;
    }

    /**
     * 获取自定义的数据权限
     *
     * @param deptIds 部门ID
     * @param role    角色
     * @return 数据权限ID
     */
    public Set<Long> getCustomize(Set<Long> deptIds, Role role) {
        Set<Dept> deptList = deptService.describeDeptByRoleId(role.getId());
        for (Dept dept : deptList) {
            deptIds.add(dept.getId());
            List<Dept> deptChildren = deptService.describeDeptListByPid(dept.getId());
            if (CollUtil.isNotEmpty(deptChildren)) {
                deptIds.addAll(deptService.describeChildDeptIdListByDeptIds(deptChildren));
            }
        }
        return deptIds;
    }
}
