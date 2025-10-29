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

package cn.odboy.framework.websocket.context;

import com.alibaba.fastjson2.JSON;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint("/websocket/{sid}")
@Getter
public class CsWsServer {
    private static final Map<String, Thread> taskThreadMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话, 需要通过它来给客户端发送数据
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
        // {username}#{bizCode}#{contextParams}
        this.sid = sid;
        CsWsClientManager.addClient(sid, this);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        CsWsMessage wsMessage = JSON.parseObject(message, CsWsMessage.class);
        String bizCode = wsMessage.getBizCode();
        Object data = wsMessage.getData();
        log.info("收到来 sid={} 的信息: message={}, bizCode={}, data={}", sid, message, bizCode, JSON.toJSONString(data));
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        try {
            CsWsClientManager.removeClient(this.sid);
        } catch (Exception e) {
            log.error("WebSocket onClose error, sid={}", this.sid, e);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        //        log.error("WebSocket sid={} 发生错误", this.sid, error);
        try {
            CsWsClientManager.removeClient(this.sid);
        } catch (Exception e) {
            log.error("WebSocket onClose error, sid={}", this.sid, e);
        }
    }

    /**
     * 实现服务器主动推送
     */
    private void innerSendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 精准推送消息
     */
    public void sendMessage(CsWsMessage message, @PathParam("sid") String sid) throws IOException {
        String body = JSON.toJSONString(message);
        //        log.info("推送消息到{}, 推送内容:{}", sid, message);
        try {
            if (sid == null) {
                sendToAll(body);
            } else {
                CsWsServer wsClient = CsWsClientManager.getClientBySid(sid);
                if (wsClient != null) {
                    wsClient.innerSendMessage(body);
                }
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * 群发消息
     */
    public void sendToAll(String message) {
        for (CsWsServer item : CsWsClientManager.getAllClient()) {
            try {
                item.innerSendMessage(message);
            } catch (IOException e) {
                log.error("发送消息给 sid={} 失败", item.sid, e);
                CsWsClientManager.removeClient(item.sid);
            }
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
        CsWsServer that = (CsWsServer) o;
        return Objects.equals(session, that.session) && Objects.equals(sid, that.sid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session, sid);
    }

    public Thread getTaskThread(String sid) {
        Thread thread = taskThreadMap.get(sid);
        if (thread == null) {
            return null;
        }
        if (thread.isInterrupted()) {
            taskThreadMap.remove(sid);
            return null;
        }
        return thread;
    }

    public void restartTask(Runnable runnable) {
        stopTask(this.sid);
        Thread thread = new Thread(runnable);
        thread.start();
        taskThreadMap.put(this.sid, thread);
    }

    public void stopTask(String sid) {
        Thread thread = getTaskThread(this.sid);
        if (thread != null) {
            thread.stop();
        }
    }
}
