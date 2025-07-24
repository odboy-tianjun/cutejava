package cn.odboy.framework.websocket.context;

import cn.odboy.base.CsObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CsWsMessage extends CsObject {
    private String bizCode;
    private String data;

    public CsWsMessage(String bizCode, String data) {
        this.bizCode = bizCode;
        this.data = data;
    }
}
