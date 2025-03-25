package cn.odboy.modules.system.mapper;

import cn.odboy.model.system.domain.Dict;
import cn.odboy.model.system.dto.DictQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DictMapper extends BaseMapper<Dict> {
    IPage<Dict> findDictPage(@Param("criteria") DictQueryCriteria criteria, Page<Object> page);
}