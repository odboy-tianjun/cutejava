package cn.odboy.config.model;

import lombok.Data;

/**
 * 登录配置
 */
@Data
public class UserLoginSettingModel {
    /**
     * 账号单用户 登录
     */
    private boolean single = false;
    private UserLoginCaptchaSettingModel captchaSetting;
}
