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

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.constant.CaptchaCodeEnum;
import cn.odboy.constant.SystemConst;
import cn.odboy.framework.properties.AppProperties;
import cn.odboy.framework.redis.KitRedisHelper;
import cn.odboy.system.constant.SystemCacheKey;
import cn.odboy.system.dal.model.response.SystemCaptchaVo;
import cn.odboy.util.KitValidUtil;
import com.wf.captcha.base.Captcha;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 验证码
 */
@Service
public class SystemCaptchaService {

  @Autowired
  private KitRedisHelper redisHelper;
  @Autowired
  private AppProperties properties;

  /**
   * 查询登录验证码
   */
  public SystemCaptchaVo getLoginCaptcha() {
    // 查询运算的结果
    Captcha captcha = properties.getLogin().getCaptchaSetting().getCaptcha();
    String uuid = SystemCacheKey.CAPTCHA_LOGIN + IdUtil.simpleUUID();
    //当验证码类型为 arithmetic时且长度 >= 2 时, captcha.text()的结果有几率为浮点型
    String captchaValue = captcha.text();
    if (captcha.getCharType() - 1 == CaptchaCodeEnum.ARITHMETIC.ordinal() && captchaValue.contains(
        SystemConst.SYMBOL_DOT)) {
      captchaValue = captchaValue.split("\\.")[0];
    }
    // 保存
    redisHelper.set(uuid, captchaValue, properties.getLogin().getCaptchaSetting().getExpiration(), TimeUnit.MINUTES);
    // 验证码信息
    SystemCaptchaVo captchaVo = new SystemCaptchaVo();
    captchaVo.setUuid(uuid);
    captchaVo.setImg(captcha.toBase64());
    return captchaVo;
  }

  /**
   * 验证验证码
   *
   * @param uuid      验证码uuid
   * @param inputCode 用户输入的验证码
   */
  public void validate(String uuid, String inputCode) {
    // 查询验证码
    String code = redisHelper.get(uuid, String.class);
    KitValidUtil.isBlank(code, "验证码不存在或已过期");
    // 清除验证码
    redisHelper.del(uuid);
    KitValidUtil.isTrue(StrUtil.isBlank(inputCode) || !code.equalsIgnoreCase(inputCode), "验证码错误");
  }
}
