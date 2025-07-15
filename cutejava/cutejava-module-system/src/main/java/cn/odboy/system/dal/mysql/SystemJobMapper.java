package cn.odboy.system.dal.mysql;

import cn.odboy.system.dal.dataobject.SystemJobTb;
import cn.odboy.system.dal.model.SystemQueryJobArgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 职位 Mapper
 *
 * @author odboy
 */
@Mapper
public interface SystemJobMapper extends BaseMapper<SystemJobTb> {
    SystemJobTb getJobByName(@Param("name") String name);

    IPage<SystemJobTb> selectJobByArgs(@Param("criteria") SystemQueryJobArgs criteria, Page<SystemJobTb> page);
}
