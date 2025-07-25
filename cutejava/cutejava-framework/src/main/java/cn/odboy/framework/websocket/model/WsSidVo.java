package cn.odboy.framework.websocket.model;

import cn.odboy.base.CsObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WsSidVo extends CsObject {
    private String username;
    private String bizCode;
    private String param;
}
