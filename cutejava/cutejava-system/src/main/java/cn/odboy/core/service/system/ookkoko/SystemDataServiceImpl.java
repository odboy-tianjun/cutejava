package cn.odboy.core.service.system.ookkoko;

import cn.hutool.core.collection.CollUtil;
import cn.odboy.core.constant.system.SystemDataScopeEnum;
import cn.odboy.core.dal.redis.system.SystemRedisKey;
import cn.odboy.core.dal.dataobject.system.SystemDeptTb;
import cn.odboy.core.dal.dataobject.system.SystemRoleTb;
import cn.odboy.core.dal.dataobject.system.SystemUserTb;
import cn.odboy.core.service.system.SystemRoleApi;
import cn.odboy.redis.RedisHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 数据权限服务实现
 */
@Service
@RequiredArgsConstructor
public class SystemDataServiceImpl implements SystemDataService {
    private final RedisHelper redisHelper;
    private final SystemRoleApi systemRoleApi;
    private final SystemDeptService systemDeptService;

    /**
     * 用户角色和用户部门改变时需清理缓存
     *
     * @param user /
     * @return /
     */
    @Override
    public List<Long> describeDeptIdListByUserIdWithDeptId(SystemUserTb user) {
        String key = SystemRedisKey.DATA_USER + user.getId();
        List<Long> ids = redisHelper.getList(key, Long.class);
        if (CollUtil.isEmpty(ids)) {
            Set<Long> deptIds = new HashSet<>();
            // 查询用户角色
            List<SystemRoleTb> roleList = systemRoleApi.describeRoleListByUsersId(user.getId());
            // 获取对应的部门ID
            for (SystemRoleTb role : roleList) {
                SystemDataScopeEnum dataScopeEnum = SystemDataScopeEnum.find(role.getDataScope());
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
    public Set<Long> getCustomize(Set<Long> deptIds, SystemRoleTb role) {
        Set<SystemDeptTb> deptList = systemDeptService.describeDeptByRoleId(role.getId());
        for (SystemDeptTb dept : deptList) {
            deptIds.add(dept.getId());
            List<SystemDeptTb> deptChildren = systemDeptService.describeDeptListByPid(dept.getId());
            if (CollUtil.isNotEmpty(deptChildren)) {
                deptIds.addAll(systemDeptService.describeChildDeptIdListByDeptIds(deptChildren));
            }
        }
        return deptIds;
    }
}
