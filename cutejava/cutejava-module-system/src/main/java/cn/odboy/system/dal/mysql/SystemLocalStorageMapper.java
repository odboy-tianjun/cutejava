package cn.odboy.system.dal.mysql;

import cn.odboy.system.dal.dataobject.SystemLocalStorageTb;
import cn.odboy.system.dal.model.SystemQueryStorageArgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 本地存储记录 Mapper
 *
 * @author odboy
 */
@Mapper
public interface SystemLocalStorageMapper extends BaseMapper<SystemLocalStorageTb> {
    IPage<SystemLocalStorageTb> selectLocalStorageByArgs(@Param("criteria") SystemQueryStorageArgs criteria, Page<SystemLocalStorageTb> page);
}
