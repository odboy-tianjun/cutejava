package cn.odboy.framework.websocket.context;

import cn.odboy.framework.websocket.constant.CsWsBizTypeEnum;
import lombok.Data;

@Data
public class CsWsMessage {
    private String message;
    private CsWsBizTypeEnum bizType;

    public CsWsMessage(String message, CsWsBizTypeEnum bizType) {
        this.message = message;
        this.bizType = bizType;
    }
}
