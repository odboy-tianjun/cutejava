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
package cn.odboy.framework.websocket;

import cn.odboy.framework.websocket.context.KitWsMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KitWsMessagePublisher {

  @Autowired
  private RedisTemplate<Object, Object> redisTemplate;

  private KitWsMessage innerBuild(String bizCode, String fromUser, String toUser, String message) {
    KitWsMessage wsMessage = new KitWsMessage();
    wsMessage.setBizCode(bizCode);

    KitWsMessage.Data data = new KitWsMessage.Data();
    data.setFormUser(fromUser);
    data.setToUser(toUser);
    data.setMessage(message);
    wsMessage.setData(data);

    return wsMessage;
  }

  public void automaticPush(String fromUser, String toUser, String message) {
    KitWsMessage wsMessage = this.innerBuild(KitWsBizCodeConst.AutomaticPush, fromUser, toUser, message);
    redisTemplate.convertAndSend("WebSocketMessage", wsMessage);
    log.info("发送AutomaticPush消息");
  }

  public void imPush(String fromUser, String toUser, String message) {
    KitWsMessage wsMessage = this.innerBuild(KitWsBizCodeConst.IM, fromUser, toUser, message);
    redisTemplate.convertAndSend("WebSocketMessage", wsMessage);
    log.info("发送IM消息");
  }
}
