package cn.odboy.core.dal.mysql.system;

import cn.odboy.core.dal.dataobject.system.SystemEmailConfigTb;
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
