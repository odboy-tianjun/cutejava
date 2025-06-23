package cn.odboy.core.service.system;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import cn.odboy.core.constant.system.SystemCaptchaBizEnum;
import cn.odboy.core.dal.dataobject.system.SystemEmailConfigTb;
import cn.odboy.core.dal.model.system.SendSystemEmailArgs;
import cn.odboy.core.dal.mysql.system.SystemEmailConfigMapper;
import cn.odboy.core.framework.templatefile.CsResourceTemplateUtil;
import cn.odboy.exception.BadRequestException;
import cn.odboy.redis.RedisHelper;
import cn.odboy.util.DesEncryptUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class SystemEmailServiceImpl extends ServiceImpl<SystemEmailConfigMapper, SystemEmailConfigTb> implements SystemEmailService {
    private final RedisHelper redisHelper;
    @Value("${app.captcha.expireTime}")
    private Long captchaExpireTime;
    @Value("${spring.application.title}")
    private String applicationTitle;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyEmailConfigOnPassChange(SystemEmailConfigTb emailConfig) throws Exception {
        SystemEmailConfigTb systemEmailConfigTb = getEmailConfig();
        if (!emailConfig.getPassword().equals(systemEmailConfigTb.getPassword())) {
            // 对称加密
            systemEmailConfigTb.setPassword(DesEncryptUtil.desEncrypt(emailConfig.getPassword()));
        }
        systemEmailConfigTb.setId(1L);
        saveOrUpdate(systemEmailConfigTb);
    }


    @Override
    public void sendEmail(SendSystemEmailArgs sendEmailRequest) {
        SystemEmailConfigTb systemEmailConfigTb = getEmailConfig();
        if (systemEmailConfigTb.getId() == null) {
            throw new BadRequestException("请先配置，再操作");
        }
        // 封装
        MailAccount account = new MailAccount();
        // 设置用户
        String user = systemEmailConfigTb.getFromUser().split("@")[0];
        account.setUser(user);
        account.setHost(systemEmailConfigTb.getHost());
        account.setPort(Integer.parseInt(systemEmailConfigTb.getPort()));
        account.setAuth(true);
        try {
            // 对称解密
            account.setPass(DesEncryptUtil.desDecrypt(systemEmailConfigTb.getPassword()));
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        account.setFrom(systemEmailConfigTb.getUser() + "<" + systemEmailConfigTb.getFromUser() + ">");
        // ssl方式发送
        account.setSslEnable(true);
        // 使用STARTTLS安全连接
        account.setStarttlsEnable(true);
        // 解决jdk8之后默认禁用部分tls协议，导致邮件发送失败的问题
        account.setSslProtocols("TLSv1 TLSv1.1 TLSv1.2");
        String content = sendEmailRequest.getContent();
        // 发送
        try {
            int size = sendEmailRequest.getTos().size();
            Mail.create(account)
                    .setTos(sendEmailRequest.getTos().toArray(new String[size]))
                    .setTitle(sendEmailRequest.getSubject())
                    .setContent(content)
                    .setHtml(true)
                    // 关闭session
                    .setUseGlobalSession(false)
                    .send();
        } catch (Exception e) {
            log.error("邮件发送失败", e);
            throw new BadRequestException("邮件发送失败");
        }
    }

    @Override
    public void checkEmailCaptcha(SystemCaptchaBizEnum biEnum, String email, String code) {
        String redisKey = biEnum.getRedisKey() + email;
        String value = redisHelper.get(redisKey, String.class);
        if (value == null || !value.equals(code)) {
            throw new BadRequestException("无效验证码");
        } else {
            redisHelper.del(redisKey);
        }
    }

    @Override
    public void sendCaptcha(SystemCaptchaBizEnum biEnum, String email) {
        if (biEnum == null) {
            throw new BadRequestException("biEnum必填");
        }
        String content;
        String redisKey = biEnum.getRedisKey() + email;
        String oldCode = redisHelper.get(redisKey, String.class);
        if (oldCode == null) {
            String code = RandomUtil.randomNumbers(6);
            // 存入缓存
            if (!redisHelper.set(redisKey, code, captchaExpireTime)) {
                throw new BadRequestException("服务异常，请联系网站负责人");
            }
            // 存在就再次发送原来的验证码
            content = CsResourceTemplateUtil.render(biEnum.getTemplateName(), Dict.create().set("code", code));
        } else {
            content = CsResourceTemplateUtil.render(biEnum.getTemplateName(), Dict.create().set("code", oldCode));
        }
        SendSystemEmailArgs sendEmailRequest = new SendSystemEmailArgs(Collections.singletonList(email), applicationTitle, content);
        sendEmail(sendEmailRequest);
    }

    @Override
    public SystemEmailConfigTb getEmailConfig() {
        SystemEmailConfigTb systemEmailConfigTb = getById(1L);
        return systemEmailConfigTb == null ? new SystemEmailConfigTb() : systemEmailConfigTb;
    }
}
