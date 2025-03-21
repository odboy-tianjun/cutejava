package cn.odboy.service;

import cn.odboy.domain.EmailConfig;
import cn.odboy.domain.dto.EmailDto;
import com.baomidou.mybatisplus.extension.service.IService;


public interface EmailService extends IService<EmailConfig> {

    /**
     * 更新邮件配置
     *
     * @param emailConfig 邮箱配置
     * @param old         /
     * @return /
     * @throws Exception /
     */
    EmailConfig config(EmailConfig emailConfig, EmailConfig old) throws Exception;

    /**
     * 查询配置
     *
     * @return EmailConfig 邮件配置
     */
    EmailConfig find();

    /**
     * 发送邮件
     *
     * @param emailDto    邮件发送的内容
     * @param emailConfig 邮件配置
     */
    void send(EmailDto emailDto, EmailConfig emailConfig);
}
