package cn.odboy.system.service;

import cn.odboy.base.KitPageArgs;
import cn.odboy.system.dal.dataobject.SystemOperationLogTb;
import cn.odboy.system.dal.model.request.SystemQueryOperationLogArgs;
import cn.odboy.system.dal.mysql.SystemOperationLogMapper;
import cn.odboy.system.framework.permission.core.KitSecurityHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemOperationLogService {

  @Autowired
  private SystemOperationLogMapper systemOperationLogMapper;

  /**
   * 查询用户操作日志 -> TestPassed
   */
  public IPage<SystemOperationLogTb> searchUserLog(KitPageArgs<SystemQueryOperationLogArgs> pageArgs) {
    LambdaQueryWrapper<SystemOperationLogTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemOperationLogTb::getUsername, KitSecurityHelper.getCurrentUsername());
    Page<SystemOperationLogTb> page = new Page<>(pageArgs.getPage(), pageArgs.getSize());
    return systemOperationLogMapper.selectPage(page, wrapper);
  }
}
