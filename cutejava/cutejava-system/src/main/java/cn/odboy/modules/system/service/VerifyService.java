package cn.odboy.modules.system.service;


import cn.odboy.model.tools.dto.EmailDto;

public interface VerifyService {
    /**
     * 发送验证码
     *
     * @param email /
     * @param key   /
     * @return /
     */
    EmailDto sendEmail(String email, String key);


    /**
     * 验证
     *
     * @param code /
     * @param key  /
     */
    void validated(String key, String code);
}
