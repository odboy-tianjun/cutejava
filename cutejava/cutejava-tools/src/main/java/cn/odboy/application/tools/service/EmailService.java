package cn.odboy.application.tools.service;

import cn.odboy.model.tools.domain.EmailConfig;
import cn.odboy.model.tools.request.SendEmailRequest;
import com.baomidou.mybatisplus.extension.service.IService;


public interface EmailService extends IService<EmailConfig> {

    /**
     * 更新邮件配置
     *
     * @param emailConfig 邮箱配置
     * @return /
     * @throws Exception /
     */
    void modifyEmailConfigOnPassChange(EmailConfig emailConfig) throws Exception;

    /**
     * 查询配置
     *
     * @return EmailConfig 邮件配置
     */
    EmailConfig describeEmailConfig();

    /**
     * 发送邮件
     *
     * @param sendEmailRequest 邮件发送的内容
     */
    void sendEmail(SendEmailRequest sendEmailRequest);

    /**
     * 发送邮件验证码
     *
     * @param email 邮箱地址
     * @param key   类型
     */
    void sendEmailCaptcha(String email, String key);
}
