package cn.odboy.system.service;

import cn.odboy.system.dal.dataobject.SystemUserTb;
import cn.odboy.system.dal.mysql.SystemUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemUserDeptService {

  @Autowired
  private SystemUserMapper systemUserMapper;

  public long countUserByDeptIds(Set<Long> deptIds) {
    LambdaQueryWrapper<SystemUserTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.in(SystemUserTb::getDeptId, deptIds);
    return systemUserMapper.selectCount(wrapper);
  }
}
