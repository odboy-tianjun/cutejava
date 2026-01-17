package cn.odboy.system.application.service;

import cn.hutool.core.collection.CollUtil;
import cn.odboy.system.application.model.CuteUserSelectVo;
import cn.odboy.system.dal.dataobject.SystemUserTb;
import cn.odboy.system.dal.model.request.SystemQueryUserArgs;
import cn.odboy.system.dal.mysql.SystemUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CuteUserSelectService {

  @Autowired
  private SystemUserMapper systemUserMapper;

  /**
   * 模糊查询用户基础信息，限制返回的条数
   *
   * @param args 查询参数
   * @return /
   */
  public List<CuteUserSelectVo> listMetadata(SystemQueryUserArgs args) {
    LambdaQueryWrapper<SystemUserTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.select(SystemUserTb::getUsername, SystemUserTb::getNickName, SystemUserTb::getDeptId, SystemUserTb::getEmail, SystemUserTb::getPhone);
    wrapper.and(c -> {
      c.eq(SystemUserTb::getPhone, args.getBlurry());
      c.or();
      c.eq(SystemUserTb::getEmail, args.getBlurry());
      c.or();
      c.like(SystemUserTb::getUsername, args.getBlurry());
      c.or();
      c.like(SystemUserTb::getNickName, args.getBlurry());
    });
    return systemUserMapper.selectPage(new Page<>(1, 50), wrapper).convert(m -> {
      CuteUserSelectVo selectVo = new CuteUserSelectVo();
      selectVo.setValue(m.getUsername());
      selectVo.setLabel(m.getNickName());
      selectVo.setDeptId(Long.toString(m.getDeptId()));
      selectVo.setEmail(m.getEmail());
      selectVo.setPhone(m.getPhone());
      return selectVo;
    }).getRecords();
  }

  public List<CuteUserSelectVo> listMetadataByUsernames(List<String> usernameList) {
    if (CollUtil.isEmpty(usernameList)) {
      return new ArrayList<>();
    }
    LambdaQueryWrapper<SystemUserTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.select(SystemUserTb::getUsername, SystemUserTb::getNickName, SystemUserTb::getDeptId, SystemUserTb::getEmail, SystemUserTb::getPhone);
    wrapper.in(SystemUserTb::getUsername, usernameList);
    return systemUserMapper.selectList(wrapper).stream().map(m -> {
      CuteUserSelectVo selectVo = new CuteUserSelectVo();
      selectVo.setValue(m.getUsername());
      selectVo.setLabel(m.getNickName());
      selectVo.setDeptId(Long.toString(m.getDeptId()));
      selectVo.setEmail(m.getEmail());
      selectVo.setPhone(m.getPhone());
      return selectVo;
    }).collect(Collectors.toList());
  }
}
