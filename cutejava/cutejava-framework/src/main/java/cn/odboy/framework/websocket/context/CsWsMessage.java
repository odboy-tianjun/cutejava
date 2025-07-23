package cn.odboy.framework.websocket.context;

import cn.odboy.base.CsObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CsWsMessage extends CsObject {
    private String bizType;
    private String data;

    public CsWsMessage(String bizType, String data) {
        this.bizType = bizType;
        this.data = data;
    }
}
