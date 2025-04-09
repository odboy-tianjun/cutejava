package cn.odboy.application.job.mapper;

import cn.odboy.model.job.domain.QuartzJob;
import cn.odboy.model.job.request.QueryQuartzJobRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface QuartzJobMapper extends BaseMapper<QuartzJob> {
    IPage<QuartzJob> queryQuartzJobPageByArgs(@Param("criteria") QueryQuartzJobRequest criteria, Page<Object> page);

    List<QuartzJob> queryActiveQuartzJob();
}
