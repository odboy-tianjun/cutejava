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

package cn.odboy.framework.properties.model;

import cn.odboy.base.CsObject;
import lombok.Getter;
import lombok.Setter;

/**
 * 密码加密传输, 前端公钥加密，后端私钥解密
 */
@Getter
@Setter
public class ContentRsaEncodeSettingModel extends CsObject {
    private String privateKey;
}
