package cn.odboy.core.dal.mysql.system;

import cn.odboy.core.dal.dataobject.system.SystemDictDetailTb;
import cn.odboy.core.dal.model.system.QuerySystemDictDetailArgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;


@Mapper
public interface SystemDictDetailMapper extends BaseMapper<SystemDictDetailTb> {
    List<SystemDictDetailTb> queryDictDetailListByDictName(@Param("name") String name);

    IPage<SystemDictDetailTb> queryDictDetailListByArgs(@Param("criteria") QuerySystemDictDetailArgs criteria, Page<Object> page);

    void deleteDictDetailByDictIds(@Param("dictIds") Set<Long> dictIds);
}
