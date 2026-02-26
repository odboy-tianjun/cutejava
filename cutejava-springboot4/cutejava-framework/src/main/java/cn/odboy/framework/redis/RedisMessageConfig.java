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
package cn.odboy.framework.redis;

import cn.odboy.framework.websocket.WsMessageSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * Redis消息队列配置
 *
 * @author odboy
 */
@Configuration
public class RedisMessageConfig {

  /**
   * 消息监听容器
   *
   * @param connectionFactory        /
   * @param listenerWsMessageAdapter /
   * @return /
   */
  @Bean
  public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerWsMessageAdapter) {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    // 指定监听的主题
    container.addMessageListener(listenerWsMessageAdapter, new ChannelTopic("DemoMessage"));
    container.addMessageListener(listenerWsMessageAdapter, new ChannelTopic("WebSocketMessage"));
    return container;
  }

  /**
   * 将订阅者与消息处理方法绑定
   *
   * @param subscriber /
   * @return /
   */
  @Bean
  public MessageListenerAdapter listenerDemoMessageAdapter(DemoMessageSubscriber subscriber) {
    return new MessageListenerAdapter(subscriber, "onMessage");
  }

  @Bean
  public MessageListenerAdapter listenerWsMessageAdapter(WsMessageSubscriber subscriber) {
    return new MessageListenerAdapter(subscriber, "onMessage");
  }
}
