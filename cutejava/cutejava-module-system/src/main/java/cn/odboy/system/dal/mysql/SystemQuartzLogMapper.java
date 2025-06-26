package cn.odboy.system.dal.mysql;

import cn.odboy.system.dal.dataobject.SystemQuartzLogTb;
import cn.odboy.system.dal.model.QuerySystemQuartzJobArgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * 定时任务日志 Mapper
 *
 * @author odboy
 */
@Mapper
public interface SystemQuartzLogMapper extends BaseMapper<SystemQuartzLogTb> {
    IPage<SystemQuartzLogTb> selectQuartzLogByArgs(@Param("criteria") QuerySystemQuartzJobArgs criteria, Page<SystemQuartzLogTb> page);
}
