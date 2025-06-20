package cn.odboy.core.dal.mysql.system;

import cn.odboy.core.dal.dataobject.system.SystemQuartzLogTb;
import cn.odboy.core.dal.model.system.QuerySystemQuartzJobArgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface SystemQuartzLogMapper extends BaseMapper<SystemQuartzLogTb> {
    IPage<SystemQuartzLogTb> queryQuartzLogPageByArgs(@Param("criteria") QuerySystemQuartzJobArgs criteria, Page<SystemQuartzLogTb> page);
}
