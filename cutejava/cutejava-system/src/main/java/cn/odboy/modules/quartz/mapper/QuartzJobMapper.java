package cn.odboy.modules.quartz.mapper;

import cn.odboy.modules.quartz.domain.QuartzJob;
import cn.odboy.modules.quartz.domain.dto.QuartzJobQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuartzJobMapper extends BaseMapper<QuartzJob> {

    IPage<QuartzJob> findAll(@Param("criteria") QuartzJobQueryCriteria criteria, Page<Object> page);

    List<QuartzJob> findAll(@Param("criteria") QuartzJobQueryCriteria criteria);

    List<QuartzJob> findByIsPauseIsFalse();
}
