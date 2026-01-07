package cn.odboy.system.dal.model;

import cn.odboy.base.KitObject;
import lombok.Getter;
import lombok.Setter;

/**
 * 登录成功后：token 与 用户信息
 */
@Getter
@Setter
public class SystemAuthVo extends KitObject {

  private String token;
  private SystemUserInfoVo user;
}
