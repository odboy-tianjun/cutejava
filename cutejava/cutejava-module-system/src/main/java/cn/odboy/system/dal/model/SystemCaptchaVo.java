package cn.odboy.system.dal.model;

import cn.odboy.base.KitObject;
import lombok.Getter;
import lombok.Setter;

/**
 * 验证码信息
 */
@Getter
@Setter
public class SystemCaptchaVo extends KitObject {

  /**
   * redis key
   */
  private String uuid;
  /**
   * image base64
   */
  private String img;
}
