package cn.odboy.core.dal.mysql.system;

import cn.odboy.core.dal.dataobject.system.SystemMenuTb;
import cn.odboy.core.dal.model.system.QuerySystemMenuArgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Mapper
public interface SystemMenuMapper extends BaseMapper<SystemMenuTb> {

    List<SystemMenuTb> queryMenuListByArgs(@Param("criteria") QuerySystemMenuArgs criteria);

    LinkedHashSet<SystemMenuTb> queryMenuSetByRoleIdsAndType(@Param("roleIds") Set<Long> roleIds, @Param("type") Integer type);

    List<SystemMenuTb> queryMenuListByPidIsNullOrderByMenuSort();

    List<SystemMenuTb> queryMenuListByPidOrderByMenuSort(@Param("pid") Long pid);

    SystemMenuTb getMenuByTitle(@Param("title") String title);

    SystemMenuTb getMenuByComponentName(@Param("name") String name);

    Integer getMenuCountByPid(@Param("pid") Long pid);

    void updateSubCntByMenuId(@Param("count") int count, @Param("menuId") Long menuId);
}
