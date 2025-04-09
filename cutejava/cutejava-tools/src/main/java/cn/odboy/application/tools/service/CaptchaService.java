package cn.odboy.application.tools.service;


import cn.odboy.model.tools.request.SendEmailRequest;

public interface CaptchaService {
    /**
     * 发送验证码
     *
     * @param email /
     * @param key   /
     * @return /
     */
    SendEmailRequest renderCodeTemplate(String email, String key);


    /**
     * 验证
     *
     * @param code /
     * @param key  /
     */
    void checkCodeAvailable(String key, String email, String code);
}
