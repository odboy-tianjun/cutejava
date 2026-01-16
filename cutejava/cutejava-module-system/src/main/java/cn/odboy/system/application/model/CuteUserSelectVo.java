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
  private String deptId;
  private String email;
  private String phone;
}
