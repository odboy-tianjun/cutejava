package cn.odboy.system.dal.mysql;

import cn.odboy.system.dal.dataobject.SystemEmailConfigTb;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 邮件配置 Mapper
 *
 * @author odboy
 */
@Mapper
public interface SystemEmailConfigMapper extends BaseMapper<SystemEmailConfigTb> {
}
