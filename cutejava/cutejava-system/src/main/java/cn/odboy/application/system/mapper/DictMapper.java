package cn.odboy.application.system.mapper;

import cn.odboy.model.system.domain.Dict;
import cn.odboy.model.system.request.QueryDictRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DictMapper extends BaseMapper<Dict> {
    IPage<Dict> queryDictPageByArgs(@Param("criteria") QueryDictRequest criteria, Page<Object> page);
}