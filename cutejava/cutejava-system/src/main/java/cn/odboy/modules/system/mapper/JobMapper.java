package cn.odboy.modules.system.mapper;

import cn.odboy.model.system.domain.Job;
import cn.odboy.model.system.dto.JobQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface JobMapper extends BaseMapper<Job> {

    @Select("select id from sys_job where name = #{name}")
    Job findByName(@Param("name") String name);

    List<Job> findAll(@Param("criteria") JobQueryCriteria criteria);

    IPage<Job> findAll(@Param("criteria") JobQueryCriteria criteria, Page<Object> page);
}