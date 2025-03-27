package cn.odboy.application.tools.service;

import cn.odboy.model.tools.domain.EmailConfig;
import cn.odboy.model.tools.dto.EmailDto;
import com.baomidou.mybatisplus.extension.service.IService;


public interface EmailService extends IService<EmailConfig> {

    /**
     * 更新邮件配置
     *
     * @param emailConfig 邮箱配置
     * @return /
     * @throws Exception /
     */
    void updateEmailConfigOnPassChange(EmailConfig emailConfig) throws Exception;

    /**
     * 查询配置
     *
     * @return EmailConfig 邮件配置
     */
    EmailConfig getEmailConfig();

    /**
     * 发送邮件
     *
     * @param emailDto 邮件发送的内容
     */
    void sendEmail(EmailDto emailDto);
}
