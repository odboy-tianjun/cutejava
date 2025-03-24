package cn.odboy.mapper;

import cn.odboy.model.tools.domain.LocalStorage;
import cn.odboy.model.tools.dto.LocalStorageQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LocalStorageMapper extends BaseMapper<LocalStorage> {

    IPage<LocalStorage> findAll(@Param("criteria") LocalStorageQueryCriteria criteria, Page<Object> page);

    List<LocalStorage> findAll(@Param("criteria") LocalStorageQueryCriteria criteria);
}
