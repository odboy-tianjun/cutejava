package cn.odboy.framework.properties.model;

import cn.odboy.base.CsObject;
import lombok.Getter;
import lombok.Setter;

/**
 * 登录配置
 *
 * @author odboy
 */
@Getter
@Setter
public class UserLoginSettingModel extends CsObject {
    /**
     * 账号单用户 登录
     */
    private boolean single = false;
    private UserLoginCaptchaSettingModel captchaSetting;
}
