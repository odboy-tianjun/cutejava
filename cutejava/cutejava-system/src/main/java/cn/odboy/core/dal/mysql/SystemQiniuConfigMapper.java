package cn.odboy.core.dal.mysql;

import cn.odboy.core.dal.dataobject.SystemQiniuConfigTb;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 七牛云配置表 Mapper
 *
 * @author odboy
 */
@Mapper
public interface SystemQiniuConfigMapper extends BaseMapper<SystemQiniuConfigTb> {
}
