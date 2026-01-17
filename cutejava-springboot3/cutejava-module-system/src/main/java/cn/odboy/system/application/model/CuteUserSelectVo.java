package cn.odboy.system.application.model;

import cn.odboy.base.KitObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CuteUserSelectVo extends KitObject {

  /**
   * username
   */
  private String value;
  /**
   * nickname
   */
  private String label;
  /**
   * 部门ID
   */
  private String deptId;
  /**
   * 邮箱
   */
  private String email;
  /**
   * 手机号
   */
  private String phone;
}
