package cn.odboy.framework.websocket.context;

import cn.odboy.framework.websocket.constant.CsWebSocketBizTypeEnum;
import lombok.Data;

@Data
public class CsWebSocketMessage {
    private String message;
    private CsWebSocketBizTypeEnum bizType;

    public CsWebSocketMessage(String message, CsWebSocketBizTypeEnum bizType) {
        this.message = message;
        this.bizType = bizType;
    }
}
