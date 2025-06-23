package cn.odboy.core.dal.mysql.system;

import cn.odboy.core.dal.dataobject.system.SystemQiniuContentTb;
import cn.odboy.core.dal.model.system.QuerySystemQiniuArgs;
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
