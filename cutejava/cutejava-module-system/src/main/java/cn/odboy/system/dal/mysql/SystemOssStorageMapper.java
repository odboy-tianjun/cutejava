package cn.odboy.system.dal.mysql;

import cn.odboy.system.dal.dataobject.SystemOssStorageTb;
import cn.odboy.system.dal.model.SystemQueryStorageArgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * OSS存储 Mapper 接口
 * </p>
 *
 * @author codegen
 * @since 2025-07-15
 */
@Mapper
public interface SystemOssStorageMapper extends BaseMapper<SystemOssStorageTb> {
    IPage<SystemOssStorageTb> selectOssStorageByArgs(SystemQueryStorageArgs criteria, Page<SystemOssStorageTb> page);

    List<SystemOssStorageTb> selectOssStorageByArgs(SystemQueryStorageArgs criteria);
}
