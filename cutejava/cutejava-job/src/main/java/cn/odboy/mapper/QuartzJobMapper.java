package cn.odboy.mapper;

import cn.odboy.model.job.domain.QuartzJob;
import cn.odboy.model.job.dto.QuartzJobQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuartzJobMapper extends BaseMapper<QuartzJob> {

    IPage<QuartzJob> selectByPage(@Param("criteria") QuartzJobQueryCriteria criteria, Page<Object> page);

    List<QuartzJob> selectByPage(@Param("criteria") QuartzJobQueryCriteria criteria);

    @Select("SELECT * FROM sys_quartz_job WHERE is_pause = 0")
    List<QuartzJob> selectActiveJob();
}
