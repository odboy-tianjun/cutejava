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

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * WebSocket客户端管理
 *
 * @author odboy
 * @date 2025-07-24
 */
@Slf4j
public class KitWsClientManager {
    /**
     * concurrent包的线程安全Map, 用来存放每个客户端对应的MyWebSocket对象。（分布式必出问题）
     */
    private static final Map<String, KitWsServer> clientMap = new ConcurrentHashMap<>();

    public static void addClient(String sid, KitWsServer wsServer) {
        // 如果存在就先删除一个, 防止重复推送消息
        removeClient(sid);
        clientMap.put(sid, wsServer);
    }

    public static void removeClient(String sid) {
        KitWsServer wsServer = clientMap.get(sid);
        if (wsServer != null) {
            try {
                log.info("关闭session, sid={}", sid);
                wsServer.getSession().close();
            } catch (Exception e) {
                log.error("Close session failed, sid={}", sid, e);
            }
            clientMap.remove(sid);
        }
    }

    public static Collection<KitWsServer> getAllClient() {
        return clientMap.values();
    }

    public static KitWsServer getClientBySid(String sid) {
        return clientMap.get(sid);
    }
}
