package cn.odboy.application.system.mapper;

import cn.odboy.model.system.domain.Job;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Set;

@Mapper
public interface UserJobMapper {
    void insertBatchWithUserId(@Param("jobs") Set<Job> jobs, @Param("userId") Long userId);

    void deleteByUserId(@Param("userId") Long userId);

    void deleteByUserIds(@Param("userIds") Set<Long> userIds);
}
