package cn.odboy.modules.system.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.odboy.domain.dto.EmailDto;
import cn.odboy.exception.BadRequestException;
import cn.odboy.modules.system.service.VerifyService;
import cn.odboy.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class VerifyServiceImpl implements VerifyService {

    @Value("${code.expiration}")
    private Long expiration;
    private final RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EmailDto sendEmail(String email, String key) {
        EmailDto emailDto;
        String content;
        String redisKey = key + email;
        // 如果不存在有效的验证码，就创建一个新的
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("email.ftl");
        String oldCode = redisUtil.get(redisKey, String.class);
        if (oldCode == null) {
            String code = RandomUtil.randomNumbers(6);
            // 存入缓存
            if (!redisUtil.set(redisKey, code, expiration)) {
                throw new BadRequestException("服务异常，请联系网站负责人");
            }
            content = template.render(Dict.create().set("code", code));
            // 存在就再次发送原来的验证码
        } else {
            content = template.render(Dict.create().set("code", oldCode));
        }
        emailDto = new EmailDto(Collections.singletonList(email), "ELADMIN后台管理系统", content);
        return emailDto;
    }

    @Override
    public void validated(String key, String code) {
        String value = redisUtil.get(key, String.class);
        if (value == null || !value.equals(code)) {
            throw new BadRequestException("无效验证码");
        } else {
            redisUtil.del(key);
        }
    }
}
