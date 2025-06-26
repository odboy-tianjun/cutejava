package cn.odboy.system.dal.mysql;

import cn.odboy.system.dal.dataobject.SystemQuartzJobTb;
import cn.odboy.system.dal.model.QuerySystemQuartzJobArgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 定时任务 Mapper
 *
 * @author odboy
 */
@Mapper
public interface SystemQuartzJobMapper extends BaseMapper<SystemQuartzJobTb> {
    IPage<SystemQuartzJobTb> selectQuartzJobByArgs(@Param("criteria") QuerySystemQuartzJobArgs criteria, Page<SystemQuartzJobTb> page);

    List<SystemQuartzJobTb> selectAllEnableQuartzJob();
}
