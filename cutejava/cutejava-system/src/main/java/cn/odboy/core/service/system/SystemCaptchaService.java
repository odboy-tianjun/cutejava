package cn.odboy.core.service.system;


import cn.odboy.core.dal.model.system.SendSystemEmailArgs;

public interface SystemCaptchaService {
    /**
     * 发送验证码
     *
     * @param email /
     * @param key   /
     * @return /
     */
    SendSystemEmailArgs renderCodeTemplate(String email, String key);


    /**
     * 验证
     *
     * @param code /
     * @param key  /
     */
    void checkCodeAvailable(String key, String email, String code);
}
