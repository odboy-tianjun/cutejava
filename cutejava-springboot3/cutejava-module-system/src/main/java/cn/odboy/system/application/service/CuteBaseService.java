package cn.odboy.system.application.service;

import cn.odboy.system.dal.dataobject.SystemDeptTb;
import cn.odboy.system.dal.mysql.SystemDeptMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

public class CuteBaseService {

  @Autowired
  private SystemDeptMapper systemDeptMapper;

  /**
   * 查询启用的所有部门集合
   *
   * @return /
   */
  public List<SystemDeptTb> listEnabledDept() {
    LambdaQueryWrapper<SystemDeptTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemDeptTb::getEnabled, 1);
    return systemDeptMapper.selectList(wrapper);
  }
}
