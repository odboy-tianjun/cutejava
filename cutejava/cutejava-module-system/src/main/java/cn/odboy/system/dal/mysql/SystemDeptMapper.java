package cn.odboy.system.dal.mysql;

import cn.odboy.system.dal.dataobject.SystemDeptTb;
import cn.odboy.system.dal.model.SystemQueryDeptArgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门 Mapper
 *
 * @author odboy
 */
@Mapper
public interface SystemDeptMapper extends BaseMapper<SystemDeptTb> {
    List<SystemDeptTb> selectDeptByArgs(@Param("criteria") SystemQueryDeptArgs criteria);

    List<SystemDeptTb> selectDeptByPid(@Param("pid") Long pid);

    int countDeptByPid(@Param("pid") Long pid);

    List<SystemDeptTb> selectDeptByPidIsNull();

    List<SystemDeptTb> selectDeptByRoleId(@Param("roleId") Long roleId);

    void updateDeptSubCountById(@Param("count") Integer count, @Param("id") Long id);
}
