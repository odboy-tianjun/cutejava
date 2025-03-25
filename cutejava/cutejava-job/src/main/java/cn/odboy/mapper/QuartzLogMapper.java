package cn.odboy.mapper;

import cn.odboy.model.job.domain.QuartzLog;
import cn.odboy.model.job.dto.QuartzJobQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface QuartzLogMapper extends BaseMapper<QuartzLog> {
    IPage<QuartzLog> findQuartzLog(@Param("criteria") QuartzJobQueryCriteria criteria, Page<Object> page);
    List<QuartzLog> findQuartzLog(@Param("criteria") QuartzJobQueryCriteria criteria);
}
