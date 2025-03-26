package cn.odboy.modules.system.mapper;

import cn.odboy.model.system.domain.Dept;
import cn.odboy.model.system.dto.DeptQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface DeptMapper extends BaseMapper<Dept> {
    List<Dept> findDept(@Param("criteria") DeptQueryCriteria criteria);

    List<Dept> findDeptByPid(@Param("pid") Long pid);

    int countDeptByPid(@Param("pid") Long pid);

    List<Dept> findDeptByPidIsNull();

    Set<Dept> findDeptByRoleId(@Param("roleId") Long roleId);

    void updateDeptSubCountById(@Param("count") Integer count, @Param("id") Long id);
}