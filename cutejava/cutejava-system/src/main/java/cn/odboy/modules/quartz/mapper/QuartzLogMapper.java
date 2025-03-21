package cn.odboy.modules.quartz.mapper;

import cn.odboy.modules.quartz.domain.QuartzLog;
import cn.odboy.modules.quartz.domain.dto.QuartzJobQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface QuartzLogMapper extends BaseMapper<QuartzLog> {

    IPage<QuartzLog> findAll(@Param("criteria") QuartzJobQueryCriteria criteria, Page<Object> page);

    List<QuartzLog> findAll(@Param("criteria") QuartzJobQueryCriteria criteria);
}
