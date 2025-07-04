package cn.odboy.framework.websocket.context;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;


@Slf4j
@Component
@ServerEndpoint("/webSocket/{sid}")
public class WebSocketServer {
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收sid
     */
    private String sid = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        this.sid = sid;
        CsWebSocketClientManager.addClient(sid, this);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        CsWebSocketClientManager.removeClient(this.sid);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来 sid={} 的信息: {}", sid, message);
        sendToAll(message);
    }

    /**
     * 群发消息
     *
     * @param message /
     */
    private static void sendToAll(String message) {
        for (WebSocketServer item : CsWebSocketClientManager.getAllClient()) {
            try {
                item.innerSendMessage(message);
            } catch (IOException e) {
                log.error("发送消息给 sid={} 失败", item.sid, e);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket sid={} 发生错误", this.sid, error);
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    private void innerSendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     */
    public static void sendMessage(CsWebSocketMessage webSocketMessage, @PathParam("sid") String sid) throws IOException {
        String message = JSON.toJSONString(webSocketMessage);
        log.info("推送消息到{}，推送内容:{}", sid, message);
        try {
            if (sid == null) {
                sendToAll(message);
            } else {
                CsWebSocketClientManager.getClientBySid(sid).innerSendMessage(message);
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WebSocketServer that = (WebSocketServer) o;
        return Objects.equals(session, that.session) &&
                Objects.equals(sid, that.sid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session, sid);
    }
}
