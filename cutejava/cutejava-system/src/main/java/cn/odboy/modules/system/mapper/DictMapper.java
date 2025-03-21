package cn.odboy.modules.system.mapper;

import cn.odboy.modules.system.domain.Dict;
import cn.odboy.modules.system.domain.dto.DictQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DictMapper extends BaseMapper<Dict> {

    IPage<Dict> findAll(@Param("criteria") DictQueryCriteria criteria, Page<Object> page);

    List<Dict> findAll(@Param("criteria") DictQueryCriteria criteria);
}