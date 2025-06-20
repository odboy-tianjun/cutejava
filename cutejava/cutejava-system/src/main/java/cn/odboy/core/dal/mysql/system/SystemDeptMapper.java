package cn.odboy.core.dal.mysql.system;

import cn.odboy.core.dal.dataobject.system.SystemDeptTb;
import cn.odboy.core.dal.model.system.QuerySystemDeptArgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface SystemDeptMapper extends BaseMapper<SystemDeptTb> {
    List<SystemDeptTb> queryDeptListByArgs(@Param("criteria") QuerySystemDeptArgs criteria);

    List<SystemDeptTb> queryDeptListByPid(@Param("pid") Long pid);

    int getDeptCountByPid(@Param("pid") Long pid);

    List<SystemDeptTb> queryDeptListByPidIsNull();

    Set<SystemDeptTb> queryDeptSetByRoleId(@Param("roleId") Long roleId);

    void updateSubCountById(@Param("count") Integer count, @Param("id") Long id);
}
