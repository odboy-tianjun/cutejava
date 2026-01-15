/*
 * Copyright 2021-2026 Odboy
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
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ServerEndpoint("/websocket/{sid}")
@Getter
public class KitWsServer {

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
    KitWsClientManager.addClient(sid, this);
  }

  /**
   * 收到客户端消息后调用的方法
   *
   * @param message 客户端发送过来的消息
   */
  @OnMessage
  public void onMessage(String message, Session session) {
    KitWsMessage wsMessage = JSON.parseObject(message, KitWsMessage.class);
    String bizCode = wsMessage.getBizCode();
    Object data = wsMessage.getData();
    log.info("收到来 sid={} 的信息: message={}, bizCode={}, data={}", sid, message, bizCode,
        JSON.toJSONString(data));
  }

  /**
   * 连接关闭调用的方法
   */
  @OnClose
  public void onClose() {
    try {
      KitWsClientManager.removeClient(this.sid);
    } catch (Exception e) {
      log.error("WebSocket onClose error, sid={}", this.sid, e);
    }
  }

  @OnError
  public void onError(Session session, Throwable error) {
    //        log.error("WebSocket sid={} 发生错误", this.sid, error);
    try {
      KitWsClientManager.removeClient(this.sid);
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
  public void sendMessage(KitWsMessage message, @PathParam("sid") String sid) {
    String body = JSON.toJSONString(message);
    //        log.info("推送消息到{}, 推送内容:{}", sid, message);
    try {
      if (sid == null) {
        sendToAll(body);
      } else {
        KitWsServer wsClient = KitWsClientManager.getClientBySid(sid);
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
    for (KitWsServer item : KitWsClientManager.getAllClient()) {
      try {
        item.innerSendMessage(message);
      } catch (IOException e) {
        log.error("发送消息给 sid={} 失败", item.sid, e);
        KitWsClientManager.removeClient(item.sid);
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
    KitWsServer that = (KitWsServer) o;
    return Objects.equals(session, that.session) && Objects.equals(sid, that.sid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(session, sid);
  }
}
