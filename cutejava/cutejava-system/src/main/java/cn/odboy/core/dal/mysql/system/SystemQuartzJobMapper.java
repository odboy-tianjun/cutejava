package cn.odboy.core.dal.mysql.system;

import cn.odboy.core.dal.dataobject.system.SystemQuartzJobTb;
import cn.odboy.core.dal.model.system.QuerySystemQuartzJobArgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SystemQuartzJobMapper extends BaseMapper<SystemQuartzJobTb> {
    IPage<SystemQuartzJobTb> queryQuartzJobPageByArgs(@Param("criteria") QuerySystemQuartzJobArgs criteria, Page<SystemQuartzJobTb> page);

    List<SystemQuartzJobTb> queryActiveQuartzJobList();
}
