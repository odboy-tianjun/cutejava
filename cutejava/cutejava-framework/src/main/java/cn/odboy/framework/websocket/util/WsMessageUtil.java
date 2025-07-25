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
