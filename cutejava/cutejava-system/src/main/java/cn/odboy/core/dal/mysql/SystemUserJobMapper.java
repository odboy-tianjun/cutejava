package cn.odboy.core.dal.mysql;

import cn.odboy.core.dal.dataobject.SystemJobTb;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * 用户职位关联 Mapper
 *
 * @author odboy
 */
@Mapper
public interface SystemUserJobMapper {
    void batchInsertUserJob(@Param("jobs") Set<SystemJobTb> jobs, @Param("userId") Long userId);

    void batchDeleteUserJob(@Param("userIds") Set<Long> userIds);
}
