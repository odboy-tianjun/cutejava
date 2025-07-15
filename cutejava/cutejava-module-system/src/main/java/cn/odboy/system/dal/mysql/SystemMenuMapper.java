package cn.odboy.system.dal.mysql;

import cn.odboy.system.dal.dataobject.SystemMenuTb;
import cn.odboy.system.dal.model.SystemQueryMenuArgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * 菜单 Mapper
 *
 * @author odboy
 */
@Mapper
public interface SystemMenuMapper extends BaseMapper<SystemMenuTb> {

    List<SystemMenuTb> selectMenuByArgs(@Param("criteria") SystemQueryMenuArgs criteria);

    LinkedHashSet<SystemMenuTb> selectMenuByRoleIdsAndType(@Param("roleIds") Set<Long> roleIds, @Param("type") Integer type);

    List<SystemMenuTb> selectMenuByPidIsNullOrderByMenuSort();

    List<SystemMenuTb> selectMenuByPidOrderByMenuSort(@Param("pid") Long pid);

    SystemMenuTb getMenuByTitle(@Param("title") String title);

    SystemMenuTb getMenuByComponentName(@Param("name") String name);

    Integer countMenuByPid(@Param("pid") Long pid);

    void updateMenuSubCntByMenuId(@Param("count") int count, @Param("menuId") Long menuId);
}
