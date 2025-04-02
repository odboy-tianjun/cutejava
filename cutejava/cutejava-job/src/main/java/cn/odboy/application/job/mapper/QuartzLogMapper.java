package cn.odboy.application.job.mapper;

import cn.odboy.model.job.domain.QuartzLog;
import cn.odboy.model.job.request.QueryQuartzJobRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface QuartzLogMapper extends BaseMapper<QuartzLog> {
    IPage<QuartzLog> queryQuartzLogPage(@Param("criteria") QueryQuartzJobRequest criteria, Page<Object> page);
}
