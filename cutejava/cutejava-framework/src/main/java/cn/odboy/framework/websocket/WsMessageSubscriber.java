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
import cn.odboy.framework.websocket.context.KitWsServer;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Ws消息订阅
 *
 * @author odboy
 */
@Slf4j
@Component
public class WsMessageSubscriber {

  @Autowired
  private KitWsServer kitWsServer;

//  /**
//   * 无效写法
//   */
//  public void onMessage(KitWsMessage message) {
//    log.info("收到来自redis消息队列的数据, {}", JSON.toJSONString(message));
//    KitWsMessage.Data data = message.getData();
//    kitWsServer.sendMessage(data.getMessage(), data.getToUsername());
//  }

  public void onMessage(String message, String channel) {
    log.info("收到来自redis消息队列的message={}, channel={}", JSON.toJSONString(message), channel);
    KitWsMessage wsMessage = JSON.parseObject(message, KitWsMessage.class);
    kitWsServer.sendMessage(wsMessage.getData().getMessage(), wsMessage.getData().getToUser());
  }
}
