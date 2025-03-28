package cn.odboy.application.system.mapper;

import cn.odboy.model.system.domain.Job;
import cn.odboy.model.system.request.JobQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface JobMapper extends BaseMapper<Job> {
    Job getJobByName(@Param("name") String name);

    IPage<Job> queryJobPage(@Param("criteria") JobQueryCriteria criteria, Page<Object> page);
}