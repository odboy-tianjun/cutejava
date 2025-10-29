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

package cn.odboy.framework.websocket.util;

import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.websocket.model.WsSidVo;
import lombok.experimental.UtilityClass;

/**
 * WebSocket消息解析工具
 *
 * @author odboy
 * @date 2025-07-25
 */
@UtilityClass
public class WsMessageUtil {
    public static WsSidVo parseSid(String sid) {
        // sid {username}_{bizCode}_{param}
        String[] sids = sid.split("_");
        if (sids.length != 3) {
            throw new BadRequestException("sid格式异常");
        }
        WsSidVo sidVo = new WsSidVo();
        sidVo.setUsername(sids[0]);
        sidVo.setBizCode(sids[1]);
        sidVo.setParam(sids[2]);
        return sidVo;
    }
}
