package cn.odboy.core.service.system;

import cn.odboy.core.constant.system.SystemCaptchaBizEnum;
import cn.odboy.core.dal.dataobject.system.SystemEmailConfigTb;
import cn.odboy.core.dal.model.system.SendSystemEmailArgs;
import com.baomidou.mybatisplus.extension.service.IService;


public interface SystemEmailService extends IService<SystemEmailConfigTb> {

    /**
     * 更新邮件配置
     *
     * @param emailConfig 邮箱配置
     * @return /
     * @throws Exception /
     */
    void modifyEmailConfigOnPassChange(SystemEmailConfigTb emailConfig) throws Exception;

    /**
     * 发送邮件
     *
     * @param sendEmailRequest 邮件发送的内容
     */
    void sendEmail(SendSystemEmailArgs sendEmailRequest);

    /**
     * 校验邮箱验证码
     */
    void checkEmailCaptcha(SystemCaptchaBizEnum biEnum, String email, String code);

    /**
     * 发送给邮箱验证码
     */
    void sendCaptcha(SystemCaptchaBizEnum biEnum, String email);

    SystemEmailConfigTb getEmailConfig();
}
