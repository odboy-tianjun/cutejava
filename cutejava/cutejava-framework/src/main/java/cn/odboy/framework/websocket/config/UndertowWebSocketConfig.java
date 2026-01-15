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
package cn.odboy.framework.websocket.config;

import io.undertow.server.DefaultByteBufferPool;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

/**
 * 解决启动io.undertow.websockets.jsr UT026010: Buffer pool was not set on WebSocketDeploymentInfo, the default pool will be used的警告
 *
 * @author odboy
 * @date 2025-10-30
 */
@Component
public class UndertowWebSocketConfig implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {

  private int calculateThreadLocalCacheSize(int processorCount) {
    // 每个I/O线程缓存2-4个缓冲区
    return Math.max(2, processorCount / 2);
  }

  private int calculateMaxPoolSize(int processorCount) {
    // 基础值 + 根据CPU核心数调整
    int baseSize = 100;
    return baseSize + (processorCount * 20);
  }

  @Override
  public void customize(UndertowServletWebServerFactory factory) {
    int processorCount = Runtime.getRuntime().availableProcessors();
    int bufferSize = 1024;
    int maxPoolSize = calculateMaxPoolSize(processorCount);
    int threadLocalCacheSize = calculateThreadLocalCacheSize(processorCount);
    factory.addDeploymentInfoCustomizers(deploymentInfo -> {
      WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo();
      webSocketDeploymentInfo.setBuffers(
          new DefaultByteBufferPool(false, bufferSize, maxPoolSize, threadLocalCacheSize));
      deploymentInfo.addServletContextAttribute("io.undertow.websockets.jsr.WebSocketDeploymentInfo",
          webSocketDeploymentInfo);
    });
  }
}
