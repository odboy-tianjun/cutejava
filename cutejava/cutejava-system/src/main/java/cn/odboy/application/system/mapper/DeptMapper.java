package cn.odboy.application.system.mapper;

import cn.odboy.model.system.domain.Dept;
import cn.odboy.model.system.request.DeptQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Set;

@Mapper
public interface DeptMapper extends BaseMapper<Dept> {
    List<Dept> selectDeptByCriteria(@Param("criteria") DeptQueryCriteria criteria);

    List<Dept> selectDeptByPid(@Param("pid") Long pid);

    int getDeptCountByPid(@Param("pid") Long pid);

    List<Dept> selectDeptByPidIsNull();

    Set<Dept> selectDeptByRoleId(@Param("roleId") Long roleId);

    void updateSubCountById(@Param("count") Integer count, @Param("id") Long id);
}