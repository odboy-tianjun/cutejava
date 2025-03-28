package cn.odboy.application.tools.mapper;

import cn.odboy.model.tools.domain.LocalStorage;
import cn.odboy.model.tools.request.LocalStorageQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LocalStorageMapper extends BaseMapper<LocalStorage> {
    IPage<LocalStorage> queryLocalStoragePage(@Param("criteria") LocalStorageQueryCriteria criteria, Page<Object> page);
}
