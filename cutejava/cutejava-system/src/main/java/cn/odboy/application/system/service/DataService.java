package cn.odboy.application.system.service;


import cn.odboy.model.system.domain.User;
import java.util.List;

/**
 * 数据权限服务类
 */
public interface DataService {

    /**
     * 获取数据权限
     *
     * @param user /
     * @return /
     */
    List<Long> describeDeptIdListByUserIdWithDeptId(User user);
}
