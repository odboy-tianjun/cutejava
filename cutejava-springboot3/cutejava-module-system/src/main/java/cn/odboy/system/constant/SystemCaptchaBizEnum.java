/*
 * Copyright 2021-2025 Odboy
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

package cn.odboy.system.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验证码业务场景
 */
@Getter
@AllArgsConstructor
public enum SystemCaptchaBizEnum {

    /* 通过手机号码重置邮箱 */
    PHONE_RESET_EMAIL_CODE("10000", "phone_reset_email_code_", "SystemEmailCaptchaTemplate.ftl",
        "通过手机号码重置邮箱"),

    /* 通过旧邮箱重置邮箱 */
    EMAIL_RESET_EMAIL_CODE("10001", "email_reset_email_code_", "SystemEmailCaptchaTemplate.ftl", "通过旧邮箱重置邮箱"),

    /* 通过手机号码重置密码 */
    PHONE_RESET_PWD_CODE("10002", "phone_reset_pwd_code_", "SystemEmailCaptchaTemplate.ftl", "通过手机号码重置密码"),

    /* 通过邮箱重置密码 */
    EMAIL_RESET_PWD_CODE("10003", "email_reset_pwd_code_", "SystemEmailCaptchaTemplate.ftl", "通过邮箱重置密码");

    /**
     * 业务编码
     */
    private final String bizCode;
    /**
     * redis中的key
     */
    private final String redisKey;
    /**
     * resources文件夹，template中的模版名称
     */
    private final String templateName;
    /**
     * 业务描述
     */
    private final String description;

    public static SystemCaptchaBizEnum getByBizCode(String bizCode) {
        for (SystemCaptchaBizEnum value : SystemCaptchaBizEnum.values()) {
            if (value.getBizCode().equals(bizCode)) {
                return value;
            }
        }
        return null;
    }
}
