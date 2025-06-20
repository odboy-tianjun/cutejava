package cn.odboy.core.dal.mysql.system;

import cn.odboy.core.dal.dataobject.system.SystemLocalStorageTb;
import cn.odboy.core.dal.model.system.QuerySystemLocalStorageArgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SystemLocalStorageMapper extends BaseMapper<SystemLocalStorageTb> {
    IPage<SystemLocalStorageTb> queryLocalStoragePageByArgs(@Param("criteria") QuerySystemLocalStorageArgs criteria, Page<SystemLocalStorageTb> page);
}
