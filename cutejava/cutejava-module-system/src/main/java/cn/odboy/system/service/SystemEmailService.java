/*
 * Copyright 2021-2026 Odboy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.odboy.system.service;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import cn.odboy.constant.SystemConst;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.properties.AppProperties;
import cn.odboy.framework.redis.KitRedisHelper;
import cn.odboy.system.constant.SystemCaptchaBizEnum;
import cn.odboy.system.dal.dataobject.SystemEmailConfigTb;
import cn.odboy.system.dal.model.request.SystemCheckEmailCaptchaArgs;
import cn.odboy.system.dal.model.request.SystemSendEmailArgs;
import cn.odboy.system.dal.mysql.SystemEmailConfigMapper;
import cn.odboy.util.KitDesEncryptUtil;
import cn.odboy.util.KitResourceTemplateUtil;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SystemEmailService {

  @Autowired
  private SystemEmailConfigMapper systemEmailConfigMapper;
  @Autowired
  private KitRedisHelper redisHelper;
  @Autowired
  private AppProperties properties;

  /**
   * 更新邮件配置 -> TestPassed
   *
   * @param emailConfig 邮箱配置
   */
  @Transactional(rollbackFor = Exception.class)
  public void updateEmailConfigById(SystemEmailConfigTb emailConfig) throws Exception {
    SystemEmailConfigTb systemEmailConfigTb = getLastEmailConfig();
    if (!emailConfig.getPassword().equals(systemEmailConfigTb.getPassword())) {
      // 对称加密
      systemEmailConfigTb.setPassword(KitDesEncryptUtil.desEncrypt(emailConfig.getPassword()));
    }
    systemEmailConfigTb.setId(1L);
    systemEmailConfigMapper.insertOrUpdate(systemEmailConfigTb);
  }

  /**
   * 发送邮件 -> TestPassed
   *
   * @param sendEmailRequest 邮件发送的内容
   */
  public void sendEmail(SystemSendEmailArgs sendEmailRequest) {
    SystemEmailConfigTb systemEmailConfigTb = getLastEmailConfig();
    if (systemEmailConfigTb.getId() == null) {
      throw new BadRequestException("请先配置, 再操作");
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
      account.setPass(KitDesEncryptUtil.desDecrypt(systemEmailConfigTb.getPassword()));
    } catch (Exception e) {
      throw new BadRequestException(e.getMessage());
    }
    account.setFrom(systemEmailConfigTb.getUser() + "<" + systemEmailConfigTb.getFromUser() + ">");
    // ssl方式发送
    account.setSslEnable(true);
    // 使用STARTTLS安全连接
    account.setStarttlsEnable(true);
    // 解决jdk8之后默认禁用部分tls协议, 导致邮件发送失败的问题
    account.setSslProtocols("TLSv1 TLSv1.1 TLSv1.2");
    String content = sendEmailRequest.getContent();
    // 发送
    try {
      int size = sendEmailRequest.getTos().size();
      Mail.create(account).setTos(sendEmailRequest.getTos().toArray(new String[size]))
          .setTitle(sendEmailRequest.getSubject()).setContent(content).setHtml(true)
          // 关闭session
          .setUseGlobalSession(false).send();
    } catch (Exception e) {
      log.error("邮件发送失败", e);
      throw new BadRequestException("邮件发送失败");
    }
  }

  /**
   * 校验邮箱验证码 -> TestPassed
   */
  public void checkEmailCaptchaV1(SystemCaptchaBizEnum biEnum, String email, String code) {
    String redisKey = biEnum.getRedisKey() + email;
    String value = redisHelper.get(redisKey, String.class);
    if (value == null || !value.equals(code)) {
      throw new BadRequestException("无效验证码");
    } else {
      redisHelper.del(redisKey);
    }
  }

  /**
   * 发送给邮箱验证码
   */
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
      if (!redisHelper.set(redisKey, code, properties.getCaptcha().getExpireTime())) {
        throw new BadRequestException("服务异常, 请联系网站负责人");
      }
      // 存在就再次发送原来的验证码
      content = KitResourceTemplateUtil.render(null, biEnum.getTemplateName(), Dict.create().set("code", code));
    } else {
      content =
          KitResourceTemplateUtil.render(null, biEnum.getTemplateName(), Dict.create().set("code", oldCode));
    }
    SystemSendEmailArgs sendEmailRequest =
        new SystemSendEmailArgs(Collections.singletonList(email), SystemConst.CURRENT_APP_TITLE, content);
    sendEmail(sendEmailRequest);
  }

  public SystemEmailConfigTb getLastEmailConfig() {
    SystemEmailConfigTb systemEmailConfigTb = systemEmailConfigMapper.selectById(1L);
    return systemEmailConfigTb == null ? new SystemEmailConfigTb() : systemEmailConfigTb;
  }

  public void checkEmailCaptcha(SystemCheckEmailCaptchaArgs args) {
    SystemCaptchaBizEnum biEnum = SystemCaptchaBizEnum.getByBizCode(args.getBizCode());
    if (biEnum == null) {
      throw new BadRequestException("不支持的业务");
    }
    this.checkEmailCaptchaV1(biEnum, args.getEmail(), args.getCode());
  }
}
