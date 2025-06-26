package cn.odboy.system.dal.mysql;

import cn.odboy.system.dal.dataobject.SystemQiniuContentTb;
import cn.odboy.system.dal.model.QuerySystemQiniuArgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 七牛云存储记录 Mapper
 *
 * @author odboy
 */
@Mapper
public interface SystemQiniuContentMapper extends BaseMapper<SystemQiniuContentTb> {
    SystemQiniuContentTb getQiniuContentByName(@Param("name") String name);

    IPage<SystemQiniuContentTb> selectQiniuContentByArgs(@Param("criteria") QuerySystemQiniuArgs criteria, Page<SystemQiniuContentTb> page);
}
