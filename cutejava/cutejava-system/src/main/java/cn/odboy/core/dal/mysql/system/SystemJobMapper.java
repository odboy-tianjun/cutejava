package cn.odboy.core.dal.mysql.system;

import cn.odboy.core.dal.dataobject.system.SystemJobTb;
import cn.odboy.core.dal.model.system.QuerySystemJobArgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SystemJobMapper extends BaseMapper<SystemJobTb> {
    SystemJobTb getJobByName(@Param("name") String name);

    IPage<SystemJobTb> queryJobPageByArgs(@Param("criteria") QuerySystemJobArgs criteria, Page<SystemJobTb> page);
}
