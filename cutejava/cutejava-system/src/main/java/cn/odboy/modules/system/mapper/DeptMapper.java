package cn.odboy.modules.system.mapper;

import cn.odboy.model.system.domain.Dept;
import cn.odboy.model.system.dto.DeptQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

@Mapper
public interface DeptMapper extends BaseMapper<Dept> {
    List<Dept> findDeptListByPage(@Param("criteria") DeptQueryCriteria criteria);
    List<Dept> findDeptListByPid(@Param("pid") Long pid);
    int countDeptByPid(@Param("pid") Long pid);
    List<Dept> findDeptListByPidIsNull();
    Set<Dept> findDeptSetByRoleId(@Param("roleId") Long roleId);
    void updateDeptSubCountById(@Param("count") Integer count, @Param("id") Long id);
}