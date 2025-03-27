package cn.odboy.application.system.service;


import cn.odboy.model.tools.dto.EmailDto;

public interface VerifyService {
    /**
     * 发送验证码
     *
     * @param email /
     * @param key   /
     * @return /
     */
    EmailDto sendEmailCode(String email, String key);


    /**
     * 验证
     *
     * @param code /
     * @param key  /
     */
    void validateEmailCode(String key, String email, String code);
}
