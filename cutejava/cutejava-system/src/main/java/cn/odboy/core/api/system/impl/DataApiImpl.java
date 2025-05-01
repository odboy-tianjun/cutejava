package cn.odboy.core.api.system.impl;

import cn.hutool.core.collection.CollUtil;
import cn.odboy.core.api.system.DataApi;
import cn.odboy.core.api.system.DeptApi;
import cn.odboy.core.api.system.RoleApi;
import cn.odboy.core.constant.DataScopeEnum;
import cn.odboy.core.constant.SystemRedisKey;
import cn.odboy.core.dal.dataobject.system.Dept;
import cn.odboy.core.dal.dataobject.system.Role;
import cn.odboy.core.dal.dataobject.system.User;
import cn.odboy.core.service.system.RoleService;
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
public class DataApiImpl implements DataApi {
    private final RedisHelper redisHelper;
    private final RoleApi roleApi;
    private final DeptApi deptApi;

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
            List<Role> roleList = roleApi.describeRoleListByUsersId(user.getId());
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
        Set<Dept> deptList = deptApi.describeDeptByRoleId(role.getId());
        for (Dept dept : deptList) {
            deptIds.add(dept.getId());
            List<Dept> deptChildren = deptApi.describeDeptListByPid(dept.getId());
            if (CollUtil.isNotEmpty(deptChildren)) {
                deptIds.addAll(deptApi.describeChildDeptIdListByDeptIds(deptChildren));
            }
        }
        return deptIds;
    }
}
