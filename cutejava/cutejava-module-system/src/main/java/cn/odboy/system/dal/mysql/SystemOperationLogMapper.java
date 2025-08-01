package cn.odboy.system.dal.mysql;

import cn.odboy.system.dal.dataobject.SystemOperationLogTb;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SystemOperationLogMapper extends BaseMapper<SystemOperationLogTb> {
}
