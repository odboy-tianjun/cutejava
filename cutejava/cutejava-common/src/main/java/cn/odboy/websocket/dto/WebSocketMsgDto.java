package cn.odboy.websocket.dto;

import cn.odboy.websocket.constant.WebSocketMsgTypeEnum;
import lombok.Data;

@Data
public class WebSocketMsgDto {
    private String msg;
    private WebSocketMsgTypeEnum msgType;

    public WebSocketMsgDto(String msg, WebSocketMsgTypeEnum msgType) {
        this.msg = msg;
        this.msgType = msgType;
    }
}
