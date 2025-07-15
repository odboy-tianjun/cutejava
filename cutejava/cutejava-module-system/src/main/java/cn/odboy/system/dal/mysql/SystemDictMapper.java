package cn.odboy.system.dal.mysql;

import cn.odboy.system.dal.dataobject.SystemDictTb;
import cn.odboy.system.dal.model.SystemQueryDictArgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 字典 Mapper
 *
 * @author odboy
 */
@Mapper
public interface SystemDictMapper extends BaseMapper<SystemDictTb> {
    IPage<SystemDictTb> selectDictByArgs(@Param("criteria") SystemQueryDictArgs criteria, Page<SystemDictTb> page);

    List<SystemDictTb> selectDictByArgs(@Param("criteria") SystemQueryDictArgs criteria);
}
