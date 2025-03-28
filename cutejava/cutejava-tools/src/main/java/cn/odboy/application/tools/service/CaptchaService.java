package cn.odboy.application.tools.service;


import cn.odboy.model.tools.dto.EmailDto;

public interface CaptchaService {
    /**
     * 发送验证码
     *
     * @param email /
     * @param key   /
     * @return /
     */
    EmailDto renderCode(String email, String key);


    /**
     * 验证
     *
     * @param code /
     * @param key  /
     */
    void checkCode(String key, String email, String code);
}
