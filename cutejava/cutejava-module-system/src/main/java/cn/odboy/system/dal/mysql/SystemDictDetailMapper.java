package cn.odboy.system.dal.mysql;

import cn.odboy.system.dal.dataobject.SystemDictDetailTb;
import cn.odboy.system.dal.model.SystemQueryDictDetailArgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;


/**
 * 字典明细 Mapper
 *
 * @author odboy
 */
@Mapper
public interface SystemDictDetailMapper extends BaseMapper<SystemDictDetailTb> {
    IPage<SystemDictDetailTb> selectDictDetailByArgs(@Param("criteria") SystemQueryDictDetailArgs criteria, Page<Object> page);

    List<SystemDictDetailTb> selectDictDetailByArgs(@Param("criteria") SystemQueryDictDetailArgs criteria);

    void deleteDictDetailByDictIds(@Param("dictIds") Set<Long> dictIds);
}
