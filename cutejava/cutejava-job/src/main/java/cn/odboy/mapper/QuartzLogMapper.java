package cn.odboy.mapper;

import cn.odboy.model.job.domain.QuartzLog;
import cn.odboy.model.job.dto.QuartzJobQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface QuartzLogMapper extends BaseMapper<QuartzLog> {
    IPage<QuartzLog> findQuartzLogPage(@Param("criteria") QuartzJobQueryCriteria criteria, Page<Object> page);
}
