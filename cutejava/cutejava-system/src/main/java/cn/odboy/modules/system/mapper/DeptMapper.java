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

    List<Dept> selectAll(@Param("criteria") DeptQueryCriteria criteria);

    List<Dept> selectByPid(@Param("pid") Long pid);
    @Select("select count(*) from sys_dept where pid = #{pid}")
    int countByPid(@Param("pid") Long pid);

    List<Dept> selectByPidIsNull();

    Set<Dept> selectByRoleId(@Param("roleId") Long roleId);

    @Select("update sys_dept set sub_count = #{count} where dept_id = #{id}")
    void updateSubCntById(@Param("count") Integer count, @Param("id") Long id);
}