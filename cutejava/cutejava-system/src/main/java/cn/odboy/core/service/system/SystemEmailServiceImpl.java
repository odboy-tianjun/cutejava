package cn.odboy.core.service.system;

import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import cn.odboy.core.dal.dataobject.system.SystemEmailConfigTb;
import cn.odboy.core.dal.mysql.system.SystemEmailConfigMapper;
import cn.odboy.core.dal.model.system.SendSystemEmailArgs;
import cn.odboy.exception.BadRequestException;
import cn.odboy.util.DesEncryptUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@CacheConfig(cacheNames = "email")
public class SystemEmailServiceImpl extends ServiceImpl<SystemEmailConfigMapper, SystemEmailConfigTb> implements SystemEmailService {
    @Lazy
    @Autowired
    private SystemEmailService emailService;
    @Autowired
    private SystemEmailApi emailApi;

    @Override
    @CachePut(key = "'config'")
    @Transactional(rollbackFor = Exception.class)
    public void modifyEmailConfigOnPassChange(SystemEmailConfigTb emailConfig) throws Exception {
        SystemEmailConfigTb localConfig = emailApi.describeEmailConfig();
        if (!emailConfig.getPassword().equals(localConfig.getPassword())) {
            // 对称加密
            emailConfig.setPassword(DesEncryptUtil.desEncrypt(emailConfig.getPassword()));
        }
        emailConfig.setId(1L);
        saveOrUpdate(emailConfig);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendEmail(SendSystemEmailArgs sendEmailRequest) {
        SystemEmailConfigTb emailConfig = emailApi.describeEmailConfig();
        if (emailConfig.getId() == null) {
            throw new BadRequestException("请先配置，再操作");
        }
        // 封装
        MailAccount account = new MailAccount();
        // 设置用户
        String user = emailConfig.getFromUser().split("@")[0];
        account.setUser(user);
        account.setHost(emailConfig.getHost());
        account.setPort(Integer.parseInt(emailConfig.getPort()));
        account.setAuth(true);
        try {
            // 对称解密
            account.setPass(DesEncryptUtil.desDecrypt(emailConfig.getPassword()));
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        account.setFrom(emailConfig.getUser() + "<" + emailConfig.getFromUser() + ">");
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
}
