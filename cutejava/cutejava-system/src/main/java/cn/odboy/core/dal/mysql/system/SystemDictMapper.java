package cn.odboy.core.dal.mysql.system;

import cn.odboy.core.dal.dataobject.system.SystemDictTb;
import cn.odboy.core.dal.model.system.QuerySystemDictArgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SystemDictMapper extends BaseMapper<SystemDictTb> {
    IPage<SystemDictTb> queryDictPageByArgs(@Param("criteria") QuerySystemDictArgs criteria, Page<SystemDictTb> page);
}
